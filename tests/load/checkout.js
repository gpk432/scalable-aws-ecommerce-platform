import http from 'k6/http';
import { check, sleep } from 'k6';
export const options = { vus: 10, duration: '20s' };
export default function () {
  const key = `k6-${__VU}-${__ITER}`;
  const res = http.post('http://localhost:8080/api/orders', JSON.stringify({customerEmail:'load@example.com',items:[{productId:1,quantity:1}]}), {headers:{'Content-Type':'application/json','Idempotency-Key':key}});
  check(res, {'created or stock exhausted': r => r.status === 201 || r.status === 409}); sleep(0.1);
}