#!/usr/bin/env bash
set -euo pipefail
BASE="${BASE_URL:-http://localhost:8080}"
echo "== Health =="; curl -fsS "$BASE/actuator/health"; echo
echo "== Products =="; curl -fsS "$BASE/api/products"; echo
KEY="demo-$(date +%s)"
BODY='{"customerEmail":"demo@example.com","items":[{"productId":1,"quantity":2}]}'
echo "== Create order =="; curl -fsS -X POST "$BASE/api/orders" -H 'Content-Type: application/json' -H "Idempotency-Key: $KEY" -d "$BODY"; echo
echo "== Retry same request =="; curl -fsS -X POST "$BASE/api/orders" -H 'Content-Type: application/json' -H "Idempotency-Key: $KEY" -d "$BODY"; echo
echo "== Demo decline (expects HTTP 409) =="; curl -sS -o /dev/stderr -w '
HTTP %{http_code}
' -X POST "$BASE/api/orders" -H 'Content-Type: application/json' -H 'Idempotency-Key: decline-demo' -d '{"customerEmail":"decline@example.com","items":[{"productId":1,"quantity":1}]}'
