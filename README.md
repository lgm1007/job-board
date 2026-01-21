# 채용공고 검색 프로젝트 (OpenSearch 실습)
### 프로젝트 개요
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
    - resources/db/migration/R__seed.sql : 샘플 데이터 적재