# Architecture notes

This repository deliberately uses a modular monolith for the checkout consistency boundary. Product inventory and the order aggregate share one PostgreSQL transaction, avoiding a distributed transaction for a workflow that benefits from strong consistency. Horizontal scaling happens at the stateless API/ECS layer.

## Key invariants
- `idempotency_key` is unique per committed order.
- Product rows are write-locked while inventory is checked and decremented.
- An order line snapshots SKU/name/price so historical orders do not mutate when the catalog changes.
- Demo payment decline raises inside the transaction, allowing inventory changes to roll back.

## Scale-up path
Real payments should use a PCI-compliant provider. For a slow/external payment authorization, a production design may move payment to an async state machine/outbox instead of holding database locks across a network call. Identity should move to OIDC/Cognito and tracing to OpenTelemetry.
