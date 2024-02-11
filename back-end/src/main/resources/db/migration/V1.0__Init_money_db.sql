create table account_payment_type
(
    id             bigserial primary key,
    account_type   varchar(20)  not null,
    payment_method varchar(255) not null unique
);

create table category
(
    id                bigserial primary key,
    deleted           boolean default false not null,
    name              varchar(255)          not null,
    group_name        varchar(255),
    created_timestamp timestamp,
    updated_timestamp timestamp
);

create table seller
(
    id                bigserial primary key,
    deleted           boolean default false not null,
    name              varchar(255)          not null,
    group_name        varchar(255),
    category_id       bigint references category (id),
    external_id       varchar(255) unique,
    address           varchar(255),
    country           varchar(255),
    town              varchar(255),
    created_timestamp timestamp,
    updated_timestamp timestamp
);

create table users
(
    id                bigserial primary key,
    name              varchar(255) not null,
    login             varchar(255) not null unique,
    password          varchar(255) not null,
    telegram_id       bigint unique,
    created_timestamp timestamp,
    updated_timestamp timestamp
);

create table account
(
    id                bigserial primary key,
    deleted           boolean default false not null,
    name              varchar(255)          not null,
    group_name        varchar(255),
    type              varchar(20)           not null,
    currency          varchar(3)            not null,
    owner_id          bigint                not null references users (id),
    created_timestamp timestamp,
    updated_timestamp timestamp
);

create table budget
(
    id                bigserial primary key,
    deleted           boolean default false not null,
    timestamp         timestamp             not null,
    currency          varchar(3)            not null,
    name              varchar(255)          not null,
    comment           varchar(255),
    user_id           bigint                not null references users (id),
    created_timestamp timestamp,
    updated_timestamp timestamp
);

create table budget_item
(
    id          bigserial primary key,
    budget_id   bigint         not null references budget (id),
    category_id bigint         not null references category (id),
    max         numeric(16, 2) not null,
    min         numeric(16, 2) not null
);

create table invoice
(
    id                bigserial primary key,
    deleted           boolean default false not null,
    timestamp         timestamp             not null,
    state             varchar(10)           not null,
    url               varchar(255)          not null unique,
    external_id       varchar(255),
    seller_id         bigint references seller (id),
    error             varchar(255),
    payment_method    varchar(255),
    content           jsonb,
    currency          varchar(3),
    total_price       numeric(16, 2),
    comment           varchar(255),
    user_id           bigint                not null references users (id),
    created_timestamp timestamp,
    updated_timestamp timestamp
);

create table item
(
    id                bigserial primary key,
    deleted           boolean default false not null,
    name              varchar(255)          not null,
    group_name        varchar(255),
    invoice_id        bigint                not null references invoice (id),
    external_id       varchar(255),
    unit              varchar(255),
    quantity          numeric(16, 2)        not null,
    unit_price        numeric(16, 2)        not null,
    price             numeric(16, 2)        not null,
    created_timestamp timestamp,
    updated_timestamp timestamp
);

create table transaction
(
    id                bigserial primary key,
    deleted           boolean default false not null,
    timestamp         timestamp             not null,
    type              varchar(10)           not null,
    account_id        bigint                not null references account (id),
    category_id       bigint references category (id),
    invoice_id        bigint unique references invoice (id),
    currency          varchar(3)            not null,
    amount            numeric(16, 2)        not null,
    comment           varchar(255),
    user_id           bigint                not null references users (id),
    created_timestamp timestamp,
    updated_timestamp timestamp
);

create table transfer
(
    id                     bigserial primary key,
    deleted                boolean default false not null,
    timestamp              timestamp             not null,
    destination_account_id bigint                not null references account (id),
    source_account_id      bigint                not null references account (id),
    currency               varchar(3)            not null,
    amount                 numeric(16, 2)        not null,
    comment                varchar(255),
    user_id                bigint                not null references users (id),
    created_timestamp      timestamp,
    updated_timestamp      timestamp,
    check ( source_account_id <> destination_account_id )
);

create table account_operation
(
    id             bigserial primary key,
    timestamp      timestamp      not null,
    type           varchar(10)    not null,
    account_id     bigint         not null references account (id),
    transaction_id bigint references transaction (id),
    transfer_id    bigint references transfer (id),
    currency       varchar(3)     not null,
    amount         numeric(16, 2) not null
);

create table user_default_account
(
    id         bigserial primary key,
    account_id bigint      not null references account (id),
    user_id    bigint      not null references users (id),
    type       varchar(50) not null,
    unique (user_id, type)
);
