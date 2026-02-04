# 채용공고 검색 프로젝트 (OpenSearch 실습)
## 프로젝트 개요
- 채용공고 검색 주제의 프로젝트
- OpenSearch 실습

## 기술스택
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- OpenSearch

## 환경설정
- 로컬 PostgreSQL 및 OpenSearch 도커 컴포즈 설치
```
docker-compose up -d
```

- 접속 정보
    - PostgreSQL: jdbc:postgresql://localhost:5432/jobboard
    - OpenSearch: http://localhost:9200
    - OpenSearch Dashboards: http://localhost:5601

- 애플리케이션 실행하면 Flyway가 DB에 샘플 데이터 적재
    - resources/db/migration/V1__init.sql : 스키마 생성
    - resources/db/migration/V2__alter_sequence.sql : PostgreSQL 시퀀스 설정 수정
    - resources/db/migration/R__seed.sql : 샘플 데이터 적재

## OpenSearch 실습 포인트
### 실습 목표
- RDB 기반의 채용공고 시스템에 OpenSearch 검색 전용 엔진으로 분리 적용
- Outbox 패턴을 활용한 비동기 인덱싱 구조
- RDB 조회 검색 vs OpenSearch 검색 역할·성능·품질 차이 비교

### RDB 조회의 한계
- 인덱스 활용 제한적
- 관련도 정렬 어려움
- 데이터가 늘어날수록 성능 급격히 저하

#### RDB 목록 조회 vs OpenSearch 검색 비교

|항목|RDB|OpenSearch|
|---|---|---|
|정확 매칭|강함|강함|
|키워드 검색|제한적|매우 강함|
|관련도 정렬|어려움|제공|
|확장성|제한|높음|

### OpenSearch 인덱스 설계
#### 인덱스 초기화 (IndexInitializer)
[JobPostingIndexInitializer.kt](src/main/kotlin/org/lgm/jobboard/search/infra/opensearch/JobPostingIndexInitializer.kt)

- 애플리케이션 시작 시 OpenSearch 인덱스가 없으면 자동 생성
- 로컬/실습 환경에 맞춘 settings & mappings 적용
- 필터링용 필드는 keyword로 설계

```
job_postings
 ├─ text   : title, description, companyName, location
 ├─ keyword: status, employmentType, companyIndustry, skillNames
 ├─ number : companyId, experienceMin, experienceMax
 └─ date   : updatedAt
```

### 인덱싱 전략: Outbox 패턴
#### Outbox 활용 배경
- DB 트랜잭션과 OpenSearch 인덱싱을 동기 처리로 묶으면,
    - OpenSearch 장애 시 → API 장애 전파
    - DB 커밋 성공, But 검색 반영 실패 → 정합성 문제

#### Outbox 패턴 흐름
1. 채용공고 생성/수정/삭제
2. DB 트랜잭션 커밋
3. `outbox_event` 테이블에 이벤트 적재
4. Outbox Poller가 이벤트를 읽어 OpenSearch 처리
   - Outbox 이벤트 스케줄러 ([OutboxScheduler.kt](src/main/kotlin/org/lgm/jobboard/outbox/infra/scheduler/OutboxScheduler.kt))

```
outbox_event
- aggregate_type = JOB_POSTING
- event_type     = UPSERT | DELETE
- payload        = { "jobPostingId": 123 }
- status         = PENDING / PROCESSING / DONE / FAILED
```

### 초기 데이터 처리: 리인덱싱 (Backfill)
- Outbox 추가 이전에 이미 존재하던 DB 데이터에 대해 OpenSearch 인덱싱
- 로컬/실습 환경에서 `ApplicationRunner` 기반 `BackfillRunner` 추가
    - [JobPostingIndexBackfillRunner.kt](src/main/kotlin/org/lgm/jobboard/search/infra/opensearch/JobPostingIndexBackfillRunner.kt)
- 실무 환경이라면 별도 배치/관리 커맨드로 분리

### OpenSearch 검색 구현
[OpenSearchJobPostingSearchAdapter.kt](src/main/kotlin/org/lgm/jobboard/search/infra/opensearch/adapter/OpenSearchJobPostingSearchAdapter.kt)

#### 1. Bool Query 구조
```
bool
 ├─ must   : 키워드 검색 (score 계산)
 └─ filter : 정확 매칭 필터 (score 영향 없음)
```

#### 2. multi_match + 필드 가중치
```
title^3
companyName^2
description
```
- 제목 (title) 가장 중요
- 회사명 2순위
- 본문 보조


#### 3. operator = AND
- 입력한 단어를 포함한 문서 우선 검색

#### 4. 키워드 (`q`) 없을 때
- `match_all` + filter
- 검색이 아닌 필터된 목록 용도
