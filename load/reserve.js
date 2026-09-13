import http from 'k6/http';
import { check } from 'k6';
import { SharedArray } from 'k6/data';

const seats = new SharedArray('seats', () => JSON.parse(open('./seats.json')));

export const options = {
  vus: 100,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<500'],
  },
};

export default function () {
  const seatId = seats[Math.floor(Math.random() * seats.length)];

  const res = http.post(
    'http://localhost:8080/api/reservations',
    JSON.stringify({ seatId: seatId, userId: crypto.randomUUID() }),
    { headers: { 'Content-Type': 'application/json' } }
  );

  check(res, { 'status is 200': (r) => r.status === 200 });
}