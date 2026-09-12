create table inventory.seat_status (
seat_id         uuid primary key,
event_id        uuid not null,
status          varchar(20) not null,
version         bigint not null default 0
);

create index idx_seat_status_event_status on inventory.seat_status (event_id, status);

create table inventory.reservations (
id              uuid primary key,
seat_id         uuid not null,
user_id         uuid not null,
status varchar(20) not null,
created_at  timestamptz not null,
expires_at  timestamptz not null
);

create index idx_reservations_seat on inventory.reservations (seat_id);