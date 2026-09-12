insert into catalog.events (id, name, venue, starts_at)
values ('11111111-1111-1111-1111-111111111111',
        'Demo concert', 'Demo arena', now() + interval '30 days');

insert into catalog.seats (id, event_id, sector, row_no, seat_no)
select gen_random_uuid(),
       '11111111-1111-1111-1111-111111111111',
       'A',
       (n / 25) + 1,
       (n % 25) + 1
from generate_series(0, 499) as n;

insert into inventory.seat_status (seat_id, event_id, status, version)
select id, event_id, 'AVAILABLE', 0
from catalog.seats;