create table catalog.events(
id          uuid primary key,
name        varchar(200) not null,
venue       varchar(200) not null,
starts_at   timestamptz not null
);

create table catalog.seats (
id          uuid primary key,
event_id    uuid not null references catalog.events (id),
sector      varchar(50) not null,
row_no      int     not null,
seat_no     int     not null,
constraint uq_seat_position unique (event_id, sector, row_no, seat_no)
);

create index idx_seats_event on catalog.seats (event_id);