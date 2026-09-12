create table orders.orders (
id          uuid primary key,
user_id     uuid   not null,
status   varchar(20) not null,
amount_cents  bigint  not null,
created_at    timestamptz not null
);