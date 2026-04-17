# Stage 08 — Deployment

**Purpose:** Plan and execute production deployment safely, with clear rollback strategy.

**Who uses this:** DevSecOps Engineers, Platform Engineers, Site Reliability Engineers

**Inputs needed:**
- Approved code (Stage 05/06/07 complete)
- Infrastructure design
- Security sign-off (Stage 07)
- NFRs for availability and deployment requirements

**Outputs produced:**
- Deployment plan with sequence, timing, and validation steps
- Infrastructure as Code (Terraform / CDK / Helm)
- Rollback plan with triggers
- Go/No-Go checklist

## Prompts in This Stage

| File | Purpose |
|---|---|
| `P1-deployment-plan.md` | Generate deployment plan |
| `P2-infrastructure-as-code.md` | Generate IaC (Terraform/CDK/Helm) |
| `P3-rollback-strategy.md` | Design rollback plan |
