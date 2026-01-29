# Demo runbook
1. `docker compose up --build`
2. Open Swagger at `http://localhost:8080/swagger-ui.html`.
3. Run `./scripts/demo.sh`.
4. Show that the repeated idempotency key returns the same public order ID.
5. Show product stock before/after checkout.
6. Trigger `decline@example.com` and explain transactional rollback.
7. Open `ProductRepository.findByIdForUpdate` and explain the inventory race.
8. Open Terraform and trace CloudFront -> WAF -> ALB -> ECS -> RDS.
