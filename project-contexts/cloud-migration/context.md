# FORGE Project Context: Cloud Migration

Load alongside `constitution/01-core-principles.md` for projects migrating on-premises or legacy systems to cloud.

---

## Project Type: Cloud Migration

**Definition:** Moving workloads from on-premises infrastructure (or legacy cloud) to a modern cloud platform (AWS, Azure, GCP).

## Migration Strategies (6 Rs)

AI should classify each component before designing:

| Strategy | Description | When to Use | AI Role |
|---|---|---|---|
| **Rehost** (Lift & Shift) | Move as-is to cloud VMs | Quick win; immediate cost savings; no modernization now | Generate IaC to replicate on-prem topology |
| **Replatform** | Move with minor optimizations (DB → managed service) | Good balance of speed and improvement | Generate migration scripts and target config |
| **Refactor** | Redesign for cloud-native (containers, serverless) | Strategic capability; team has capacity | Full SDLC approach; use Greenfield + Cloud context |
| **Repurchase** | Replace with SaaS | Commodity function better served by SaaS | Evaluate options; generate integration design |
| **Retire** | Decommission | Legacy system with no active users | Document dependencies; generate decommission plan |
| **Retain** | Keep on-premises | Regulatory data residency; unacceptable migration risk | Document rationale; design hybrid integration |

## AI Behavior for Cloud Migration

**Classification phase (before design):**
- For each workload, determine which R strategy is appropriate
- Do not default to Refactor — it's the most expensive and risky
- Rehost first for migration velocity; Refactor later for optimization

**AWS-specific patterns (customize for Azure/GCP):**
- EC2 (Rehost) → ECS Fargate or EKS (Replatform/Refactor)
- On-prem Oracle DB → RDS Oracle → RDS Aurora PostgreSQL
- On-prem MQ → Amazon MQ → Amazon MSK (Kafka)
- On-prem file servers → S3 + lifecycle policies
- On-prem batch → AWS Batch or Step Functions
- On-prem API gateway → Amazon API Gateway

**Security in cloud migration:**
- Zero-trust model: no implicit trust based on network location
- IAM roles for all service-to-service auth (no service accounts with passwords)
- Encryption: all data encrypted at rest (KMS) and in transit (TLS 1.2+)
- VPC design: private subnets for compute, isolated subnets for databases
- No public-facing endpoints except through approved ingress (ALB + WAF)

**Well-Architected Framework lens:**
Apply these pillars to every cloud design:
1. Operational Excellence — automate operations, refine procedures
2. Security — protect data, systems, assets
3. Reliability — recover from failures, meet demand
4. Performance Efficiency — use resources efficiently
5. Cost Optimization — avoid unnecessary costs
6. Sustainability — minimize environmental impact

## Cloud Migration SDLC Emphasis

- Stage 00 (Pre-Engagement): T3 (Integration Map) is critical — map all on-prem dependencies
- Stage 01 (Discovery): Application portfolio assessment — classify all workloads by R strategy
- Stage 03 (Architecture): Landing zone design, VPC, shared services
- Stage 08 (Deployment): Cutover planning is complex — network cutovers, DNS changes, data migration timing
