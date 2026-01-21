-- R__seed.sql
-- 로컬 실습용 seed 데이터 (Repeatable)

-- 회사
insert into company (name, website, industry)
values
    ('아정네트웍스', 'https://www.ajd.co.kr', 'IT'),
    ('모요', 'https://www.moyoplan.com', 'IT'),
    ('세컨신드롬', 'https://www.dalock.kr/service/main', 'IT'),
    ('하입앤컴퍼니', 'https://www.artness.kr/company', 'LIFESTYLE'),
    ('헬스커넥트', 'https://www.hconnect.co.kr', 'HEALTHCARE'),
    ('백패커', 'https://www.idus.com', 'IT'),
    ('피비지', 'https://www.printbakery.com', 'LIFESTYLE'),
    ('자비스앤빌런즈', 'https://jobisnvillains.com', 'TAX'),
    ('티웨이브', 'https://iminapp.kr', 'FINTECH'),
    ('원스토어', 'https://www.onestorecorp.com', 'IT'),
    ('팀플러스', 'https://ticketbayrecruit.notion.site', 'IT'),
    ('고위드', 'https://www.gowid.com', 'FINTECH'),
    ('라포랩스', 'https://www.queen-it.com', 'IT'),
    ('레브잇', 'https://team.alwayz.co', 'IT'),
    ('힐링페이퍼', 'https://blog.gangnamunni.com', 'HEALTHCARE'),
    ('커넥트웨이브', 'https://connectwave.co.kr', 'IT'),
    ('와드', 'https://www.catchtable.co.kr', 'IT'),
    ('위대한상상', 'https://www.wesang.com', 'IT'),
    ('엔라이즈', 'https://www.nrise.net', 'IT')
    on conflict do nothing;

-- 공고
-- (회사 id는 name으로 찾아서 넣음: DB 의존성 줄이기)
insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '[아정당] 플랫폼 백엔드 개발자 (3년 이상~5년 이하)',
       '자사 신사업 서비스를 위한 백엔드 시스템을 신규 개발 및 고도화 합니다. Kotlin + SpringBoot 기반의 API 서버를 설계하고 구현합니다. 대졸 이상 / 백엔드 개발 경력 3년 ~ 5년이신 분. 객체지향 기반의 설계/구현에 능숙하여 테스트 가능한 구조로 개발하시는 분. Kotlin, Java, SpringBoot 기반의 RESTful API 개발 경험이 있으신 분. JPA, Hibernate 기반 ORM 활용 및 도메인 모델링 설계 경험이 있으신 분. AWS 기반 시스템을 직접 운영해본 경험이 있으신 분. 비즈니스 로직에 대한 이해를 바탕으로 레거시 코드 리팩토링을 주도해 본 경험. 대용량 트래픽 및 데이터 처리에 대한 경험이 있으신 분',
       'FULL_TIME', 3, 5,
       'Seoul', 'OPEN'
from company c where c.name = '아정네트웍스'
on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       'Backend Developer - Product',
       '이런 기술을 사용해요: Kotlin, Spring Boot(MVC), JPA/Hibernate, Querydsl, MySQL, Redis, AWS(EKS, RDS, ElastiCache), Datadog, 이런 업무를 해요: 스쿼드 내 PO, 디자이너, 프론트엔드 등과 긴밀하게 협업해요. 신규 비즈니스 기회를 탐색하고 기존 제품을 성장시키기 위한 백엔드 개발을 맡아요. 서비스의 안정성과 확장성을 고려하여 기술적 결정을 내리고 아키텍처를 설계해요. API 개발, 데이터 기반 가설 검증, AB 테스트 등을 주도해요. 사용자 증가와 함께 발생하는 기술적 과제를 해결해요.',
       'FULL_TIME', 5, null,
       'Seoul', 'OPEN'
from company c where c.name = '모요'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '백엔드 엔지니어',
       '3년 이상의 백엔드 개발 경력, Kotlin(or Java) 및 웹 프레임워크(Spring Boot) 활용 경험 필수. RDS(MySQL, PostgreSQL)의 성능과 트랜잭션에 대한 이해도가 있는 분. 클라우드(AWS or GCP, Azure,NHN Cloud 등) 환경에서 기반 서비스 운영 경험 필수. 자기주도적 업무 추진 및 문제 해결력, 원활한 협업 및 커뮤니케이션 능력. AWS를 활용하여 서비스 아키텍처 설계한 경험과 이해도가 있는 분. Python에 대한 이해와 Django(혹은 DRF)를 활용한 서비스 개발 경험이 있는 분. 스타트업 환경에서 Agile 문화를 경험하고 이해도가 있는 분',
       'FULL_TIME', 3, null,
       'Seoul', 'OPEN'
from company c where c.name = '세컨신드롬'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '서버 개발 담당자',
       'Java 및 Spring Framework를 활용한 백엔드 개발 경력 3년 이상. Restful API 설계와 개발 경험. 백엔드 아키텍처 설계와 개선 경험. 팀 협업과 커뮤니케이션에 대한 경험. 클라우드(AWS 등) 및 컨테이너 기반 (EKS, ECS) 서비스 개발 경험. CI/CD 환경에서 배포 자동화를 구축·운영해본 경험이 있으신 분. 신규 기술 학습에 적극적이고 거부감이 없는 분',
       'FULL_TIME', 3, null,
       'Seoul', 'OPEN'
from company c where c.name = '하입앤컴퍼니'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '자바기반 풀스택 개발자',
       'Spring, Node.js 기반 백엔드 시스템 개발 및 유지보수.  Spring 기반 백엔드 및 React 프론트엔드 개발 가능자. Node.js 기반 백엔드 개발 가능자. RDBMS(Maria DB, MySQL, Oracle, PostgreSQL 등), NoSQL 기반 개발 역량 보유. 팀워크를 중시하며 원활한 커뮤니케이션 능력을 갖추신 분. Nextjs 프로젝트 경험자. PostgreSQL 경험자. 하이브리드앱 서비스 경험자. 다양한 Cloud 구성 경험자',
       'FULL_TIME', 3, null,
       'Seoul', 'OPEN'
from company c where c.name = '헬스커넥트'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '[아이디어스] 백엔드 개발자 (Java/PHP)',
       'RESTful API 서버 개발 경험 및 운영 경험이 있으신 분. RDB, NoSQL을 사용한 개발 및 운영 경험이 있으신 분. PHP와 데이터베이스를 이용하여 웹 애플리케이션 개발 경험이 2년 이상이신 분. JAVA 백엔드 실무 개발 경력 2년 이상이신 분. Spring 프레임워크를 사용한 서버 개발 및 운영 경험이 있으신 분. Restful API와 같은 HTTP를 이용한 API 서비스 개발 경험이 있는 분. OOP에 대한 이해와 활용이 가능한 분. 코드 리뷰의 중요성에 대해 이해하고, 거부감이 없는 분. ORM(JPA, Hibernate 등)을 활용한 개발 및 운영 경험이 있으신 분. 도커 및 오케스트레이션(Kubernetes)에 대한 이해와 개발 및 운영 경험이 있으신 분. Elastic Search에 대한 이해와 개발 및 운영 경험이 있으신 분',
       'FULL_TIME', 4, 5,
       'Seoul', 'OPEN'
from company c where c.name = '백패커'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '커머스 백엔드 개발 (5년 이상)',
       '백엔드 개발 경력 5년 이상. Node.js / Java / Spring / Python(Django/FastAPI) 중 1개 이상 숙련. MySQL/PostgreSQL 등 RDBMS 경험. REST API 개발 경험. 커머스 도메인 경험 필수, 웹앱 경험. Git 협업 능력. AWS/GCP 클라우드 운영 경험. Redis/Queue/Kafka 등 메시징 구조 경험. MSA 및 대규모 트래픽 대응 경험. ElasticSearch/알고리아 검색 엔진 경험. CI/CD, 모니터링 등 DevOps 경험',
       'FULL_TIME', 5, null,
       'Seoul', 'OPEN'
from company c where c.name = '피비지'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       'Backend Engineer(세무기장 서비스)',
       '3년 이상의 백엔드 개발 경력을 보유하신 분. 2년 이상 Java, Spring Framework, JPA를 활용하여 일한 경력이 있으신 분. 복잡한 비즈니스 로직과 레거시 시스템을 다루며, 비즈니스 임팩트를 고려한 리팩터링 경험이 있으신 분. MySQL DB에 대해 이해도가 높으신 분. Microservices Architecture 기반의 시스템 개발 경험이 있으신 분. Kafka, Redis를 활용하여 개발한 경험이 있는 사람. AWS 등의 클라우드 기반 서비스를 개발하고 운영한 경험이 있으신 분. 대용량 데이터 처리를 위한 DBMS 설계 및 운영 경험이 있으신 분',
       'FULL_TIME', 3, null,
       'Seoul', 'OPEN'
from company c where c.name = '자비스앤빌런즈'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '미드레벨 백엔드 엔지니어 (4년 이상~6년 이하)',
       '백엔드 개발 경력 4~6년. Spring Boot 기반 실무 프로젝트 경험. RDBMS 설계, 쿼리 최적화 및 성능 튜닝 경험. RESTful API 설계 경험. 클라우드 환경(AWS, GCP, Azure 등) 기반 서비스 개발·운영 경험. 대용량 데이터 처리 및 분산 시스템(Kafka, RabbitMQ 등) 운영 경험. MSA(Microservices Architecture) 환경 개발 및 운영 경험. 이벤트 기반 아키텍처(EDA) 설계·구현 경험. Kotlin 사용 경험. 대규모 트래픽 서비스 개발·운영 경험',
       'FULL_TIME', 4, 6,
       'Seoul', 'OPEN'
from company c where c.name = '티웨이브'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '웹 서비스 개발자',
       'Frontend 혹은 Backend 개발/운영 경험. Kotlin,JAVA, HTML, CSS, JavaScript 를 익숙하게 사용 가능한 분. REST API 설계 및 WebSocket 에 대한 지식을 갖추신 분. 반응형 디자인, 웹 접근성, 웹 표준을 고려한 개발 경험. CI/CD 에 대한 이해 및 경험. React 컴포넌트 기반의 화면개발 이해. Jenkins 또는 CI/CD를 활용한 빌드/배포 관리 경험',
       'FULL_TIME', 5, 15,
       'Gwacheon', 'OPEN'
from company c where c.name = '원스토어'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '[티켓베이] Backend 엔지니어 (3년 이상)',
       'Java와 스프링 생태계 기반의 웹, API 개발 실무 경험. 대용량 데이터 처리를 위한 데이터베이스(DB) 설계 및 쿼리 최적화 (RDB, NoSQL). 백엔드 시스템 아키텍처 설계 및 구현 경험. 운영 환경에서 ORM 사용 경험이 있는 분 경험. MSA(Microservices Architecture) 기반 개발 경험. RESTful / gRPC API 설계 및 개발 경험. Docker / Kubernetes(EKS, ECS) 기반 컨테이너 환경 운영 경험. 주 사용 언어 자바 외 다수 언어/프레임워크 활용 능력 (Kotlin, Python, Node.js, Go 등). 캐싱, 메시지 큐(Kafka, RabbitMQ 등) 등 미들웨어 활용 경험. 코드 리뷰 문화 및 테스트 자동화 경험. 화이트보드에 자신의 설계, 아이디어를 동료들에게 설명하실 수 있는 분',
       'FULL_TIME', 3, 7,
       'Seoul', 'OPEN'
from company c where c.name = '팀플러스'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '[신사업] Backend Engineer (백엔드 개발자)',
       '3년 이상 7년 이하의 백엔드 개발 경력 또는 그에 준하는 역량을 가지고 계신 분. PM/사업부와의 협업을 통해 요구사항을 빠르게 이해하고 실행으로 옮길 수 있는 분. RDBMS 설계 및 최적화 경험이 있으신 분. 레거시 프로젝트를 신규 프로젝트로 리팩토링 한 경험이 있으신 분. 테스트 코드를 통한 소프트웨어 품질 개선 경험이 있으신 분. 비즈니스, 프로덕트, 개발자 등 다양한 이해관계자들과 합리적이고 유연한 커뮤니케이션이 가능한 분.',
       'FULL_TIME', 3, 7,
       'Seoul', 'OPEN'
from company c where c.name = '고위드'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       'Server Engineer : Scale Squad',
       '서버 개발 경력 3년 이상 또는 그에 준하는 역량을 갖추신 분. 단순 기능 구현을 이상으로 도메인 구조와 시스템 설계를 주도적으로 고민해 보신 분. 복잡도 높은 업무를 돕는 B2B 서비스나 SaaS 툴에 관심이 많으신 분. 조직 내에서 비효율적인 업무 방식에 대해 챌린지 한 경험이 있으신 분. B2B 서비스나 SaaS 툴을 직접 설계·구현해 본 경험이 있으신 분. 커머스 도메인에 관심이 많고, 데이터를 보며 도메인 이해도를 넓히고 싶은 분.',
       'FULL_TIME', 3, null,
       'Seoul', 'OPEN'
from company c where c.name = '라포랩스'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       'Software Engineer (Backend)',
       '만 2년 이상의 백엔드 개발 경력에 준하는 역량을 보유하신 분이 필요합니다. 프로그래밍 언어(Kotlin/Java 또는 Typescript), 프레임워크(Spring Boot 또는 Node.js) 활용 능력 및 소프트웨어 개발 역량이 있으신 분이 필요합니다. 비즈니스 요건을 빠르게 이해하고 능숙하게 데이터 모델과 API를 디자인할 수 있는 분이 필요합니다. 빠르게 개발하고 배포하는 조직에서 주도적으로 일해 보신 분이 필요합니다. 고가용성의 확장 가능한 시스템을 설계하고 운영해본 경험이 있는 분이 필요합니다.',
       'FULL_TIME', 2, null,
       'Seoul', 'OPEN'
from company c where c.name = '레브잇'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '[플랫폼 사업] 백엔드 개발자',
       'Kotlin, Spring Boot를 활용한 강남언니 백엔드 서비스 설계 및 개발. 웹 서버 엔지니어링 경험과 역량을 갖춘 분. 하나 이상의 OO(Object-Oriented) 프로그래밍 언어를 능숙하게 다루는 분. 문제의 본질을 이해하고 답을 찾기 위해 동료의 의견에 집중하는 분. 모듈화에 대한 설계 원칙들의 개념과 장단점을 이해하는 분. HTTP API의 장단점을 이해하는 분. 서버 시스템 아키텍처 설계 패턴들의 개념과 장단점을 이해하는 분. CQRS(Command Query Responsibility Segregation)를 이해하는 분. 2년 이상의 NoSQL 데이터베이스 운영 경험을 가진 분. 테스트 주도 개발 이해와 경험을 가진 분. 1년 이상의 Continuous Integration과 Continuous Deployment 운영 경험을 가진 분',
       'FULL_TIME', 3, null,
       'Seoul', 'OPEN'
from company c where c.name = '힐링페이퍼'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '[다나와개발본부] Backend Engineer (3년 이상)',
       'Python 및 Django, Flask, FastAPI framework 기반의 어플리케이션 개발 경험이 있으신 분. Java/Kotlin/Spring/JPA 기반의 어플리케이션 개발 경험이 있으신 분. RESTful API의 설계 규칙을 따르며 개발한 경험이 있으신 분. Git을 이용해 소프트웨어 버전 관리를 하고 PR, 코드 리뷰 등의 과정에 익숙하신 분. 백엔드 애플리케이션 및 배치 프로세스 개발 경험이 있으신 분. Celery, RabbitMQ, Kafka 등 분산/비동기 처리에 활용되는 기술에 대한 이해와 경험이 있으신 분. NoSQL(Redis, MongoDB 등)를 사용한 서비스 개발 경험이 있으신 분. ElasticSearch 서비스 적용 경험이 있으신 분. CI/CD 도구를 이용해 빌드/배포한 경험이 있으신 분. MSA와 컨테이너화 기술(docker, k8s 등)에 대한 이해도가 있으신 분.',
       'FULL_TIME', 3, 8,
       'Seoul', 'OPEN'
from company c where c.name = '커넥트웨이브'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '광고비지니스셀 Back_End Developer',
       '백엔드 개발 경력 5년 이상. 시스템 아키텍처 설계 또는 기술 의사결정 리딩 경험. 레거시 리팩토링 또는 대규모 서비스 구조 개선 경험. 고가용성·대용량 트래픽 시스템 설계·개발·운영 경험. 하나 이상의 프로그래밍 언어에 대한 깊은 이해와 숙련도. 유연하고 확장성 있는 HTTP API 설계 역량.',
       'FULL_TIME', 5, null,
       'Seongnam', 'OPEN'
from company c where c.name = '와드'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       '[Tech] Java Backend Developer - 정산시스템 운영/개발',
       'Java 및 Spring Framework를 활용한 백엔드 개발 경력 5년 이상. MSA(Microservices Architecture) 환경에서의 개발 및 운영 경험. RDBMS 및 NoSQL 데이터베이스 설계 및 최적화 경험. RESTful API 설계 및 구현 경험. 대규모 트래픽 처리 및 성능 최적화 경험. 클라우드 환경에서의 서비스 배포 및 운영 경험. Git, CI/CD 도구(Jenkins, ArgoCD 등)를 활용한 협업 및 자동화 경험. Kafka, AWS SNS/SQS 등 메시지 큐 시스템 사용 경험. Kubernetes 및 컨테이너 환경(Docker) 활용 경험',
       'FULL_TIME', 5, null,
       'Seoul', 'OPEN'
from company c where c.name = '위대한상상'
    on conflict do nothing;

insert into job_posting (
    company_id, title, description, employment_type, experience_min, experience_max,
    location, status
)
select c.id,
       'Kotlin Backend Engineer',
       'Python, Kotlin 등 자신있는 프로그래밍 언어를 1개 이상 갖추신 분. RDBMS의 특성을 이해하고 쿼리를 구성할 수 있는 분. 비즈니스 임팩트를 고민하고, 고객 관점에서 문제를 바라보시는 분. 서비스와 인프라의 문제점을 찾고 개선해 나갈 수 있는 분. 기술 부채를 해결하기 위해 확장성 높은 설계와 구현을 지향하시는 분. 문제를 해결하기 위해, 동료와 능동적이고 적극적으로 소통하시는 분. AI 플랫폼(GPT, Claude 등)을 업무에 적극적으로 활용하고 싶으신 분. 고객에 대한 호기심과 고객에게 더 높은 가치 전달에 관심을 가진 분. 지속적인 서비스 개선과 장애 대응 경험을 가진 분.',
       'FULL_TIME', 3, null,
       'Seoul', 'OPEN'
from company c where c.name = '엔라이즈'
    on conflict do nothing;

-- 스킬
insert into skill (name)
values
    ('Kotlin'), ('Java'), ('Spring Boot'), ('Node.js'), ('Nest.js'),
    ('Python'), ('FastAPI'), ('Django'), ('Javascript'), ('React'),
    ('MySQL'), ('PostgreSQL'), ('MongoDB'), ('Redis'), ('Kafka'), ('AWS')
    on conflict do nothing;

-- 공고-스킬 매핑
insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Java', 'Spring Boot', 'AWS')
where c.name = '아정네트웍스' and jp.title like '[아정당] 플랫폼 백엔드 개발자%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Spring Boot', 'MySQL', 'Redis', 'AWS')
where c.name = '모요' and jp.title like 'Backend Developer%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Java', 'Spring Boot', 'MySQL', 'PostgreSQL', 'AWS')
where c.name = '세컨신드롬' and jp.title like '백엔드 엔지니어%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Java', 'Spring Boot', 'AWS')
where c.name = '하입앤컴퍼니' and jp.title like '서버 개발 담당자%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Java', 'Spring Boot', 'Node.js', 'MySQL', 'PostgreSQL', 'React')
where c.name = '헬스커넥트' and jp.title like '자바기반 풀스택 개발자%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Java', 'Spring Boot', 'AWS')
where c.name = '백패커' and jp.title like '[아이디어스] 백엔드 개발자%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Java', 'Spring Boot', 'Node.js', 'Python', 'FastAPI', 'Django', 'MySQL', 'PostgreSQL', 'AWS')
where c.name = '피비지' and jp.title like '커머스 백엔드 개발%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Java', 'Spring Boot', 'MySQL', 'MongoDB', 'Redis', 'Kafka', 'AWS')
where c.name = '자비스앤빌런즈' and jp.title like 'Backend Engineer%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Java', 'Spring Boot', 'MySQL', 'PostgreSQL', 'Redis', 'Kafka', 'AWS')
where c.name = '티웨이브' and jp.title like '미드레벨 백엔드 엔지니어%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Java', 'Javascript', 'React')
where c.name = '원스토어' and jp.title like '웹 서비스 개발자%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Java', 'MySQL', 'MongoDB')
where c.name = '팀플러스' and jp.title like '[티켓베이] Backend 엔지니어%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Java', 'Spring Boot', 'MySQL', 'Redis')
where c.name = '고위드' and jp.title like '[신사업] Backend Engineer (백엔드 개발자)%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Spring Boot', 'MySQL', 'Redis', 'Kafka')
where c.name = '라포랩스' and jp.title like 'Server Engineer%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Java', 'Spring Boot', 'Node.js', 'MySQL', 'PostgreSQL', 'MongoDB', 'Redis', 'AWS')
where c.name = '레브잇' and jp.title like 'Software Engineer (Backend)%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Java', 'Spring Boot', 'Node.js', 'MySQL', 'MongoDB', 'AWS')
where c.name = '힐링페이퍼' and jp.title like '[플랫폼 사업] 백엔드 개발자%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Java', 'Spring Boot', 'Python', 'FastAPI', 'Redis', 'MongoDB')
where c.name = '커넥트웨이브' and jp.title like '[다나와개발본부] Backend Engineer%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Java', 'Spring Boot', 'Kafka', 'AWS')
where c.name = '와드' and jp.title like '광고비지니스셀 Back_End Developer%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Java', 'Spring Boot', 'MySQL', 'MongoDB', 'Kafka', 'AWS')
where c.name = '위대한상상' and jp.title like '[Tech] Java Backend Developer - 정산시스템 운영/개발%'
    on conflict do nothing;

insert into job_posting_skill (job_posting_id, skill_id)
select jp.id, s.id
from job_posting jp
         join company c on c.id = jp.company_id
         join skill s on s.name in ('Kotlin', 'Spring Boot', 'Python', 'FastAPI', 'Django', 'MySQL', 'Redis', 'AWS')
where c.name = '엔라이즈' and jp.title like 'Kotlin Backend Engineer%'
    on conflict do nothing;

-- Outbox seed는 보통 넣지 않음(앱이 생성)
