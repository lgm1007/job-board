-- V1__init.sql

-- 회사
create table if not exists company (
    id              bigserial primary key,
    name            varchar(200) not null,
    website         varchar(500),
    industry        varchar(100),
    created_at      timestamptz not null default now(),
    updated_at      timestamptz not null default now()
    );

-- 채용공고 (원본)
create table if not exists job_posting (
    id                  bigserial primary key,
    company_id          bigint not null references company(id),

    title               varchar(300) not null,     -- 예: Backend Engineer (Kotlin)
    description         text not null,             -- JD 본문(원본)

    employment_type     varchar(50) not null,      -- FULL_TIME, CONTRACT, INTERN
    experience_min      int not null default 0,    -- 최소 경력(년)
    experience_max      int,                       -- 최대 경력(년), null이면 무관
    location            varchar(200) not null,     -- 예: Seoul

    status              varchar(30) not null default 'OPEN', -- OPEN, CLOSED
    created_at          timestamptz not null default now(),
    updated_at          timestamptz not null default now()
    );

-- 기술 스택 마스터 (선택: 통제된 목록을 두고 싶다면)
create table if not exists skill (
     id          bigserial primary key,
     name        varchar(100) not null unique
    );

-- 공고-기술스택 매핑 (N:M)
create table if not exists job_posting_skill (
    job_posting_id  bigint not null references job_posting(id) on delete cascade,
    skill_id        bigint not null references skill(id),
    primary key (job_posting_id, skill_id)
    );

-- Outbox 이벤트 (색인 이벤트)
create table if not exists outbox_event (
    id              bigserial primary key,
    aggregate_type  varchar(50) not null,       -- 예: JOB_POSTING
    aggregate_id    bigint not null,            -- job_posting.id
    event_type      varchar(50) not null,       -- 예: UPSERT, DELETE
    payload         jsonb not null,             -- 색인에 필요한 최소 데이터 스냅샷/메타
    status          varchar(30) not null default 'PENDING', -- PENDING, PROCESSING, DONE, FAILED
    attempts        int not null default 0,
    available_at    timestamptz not null default now(),     -- 재시도 지연 처리
    last_error      text,
    created_at      timestamptz not null default now(),
    updated_at      timestamptz not null default now()
    );

-- 인덱스들(조회/폴링 최적화)
create index if not exists idx_job_posting_company_id on job_posting(company_id);
create index if not exists idx_job_posting_status on job_posting(status);

create index if not exists idx_outbox_status_available on outbox_event(status, available_at);
create index if not exists idx_outbox_aggregate on outbox_event(aggregate_type, aggregate_id);
create index if not exists idx_outbox_payload_gin on outbox_event using gin (payload);
