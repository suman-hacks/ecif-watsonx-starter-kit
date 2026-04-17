# AWS Q Developer — Setup and FORGE Integration Guide

> This guide covers installation, configuration, and FORGE-aligned workflows for AWS Q Developer — Amazon's AI coding assistant optimized for AWS-native workloads, cloud migration, and AWS service integration.

---

## What is AWS Q Developer?

AWS Q Developer (formerly Amazon CodeWhisperer, now part of the broader AWS Q family) is Amazon's AI coding assistant. It provides:

- **Inline code completion** across all major programming languages, with particular strength in AWS SDK usage patterns
- **Q Developer Chat** for conversational code assistance, architecture guidance, and AWS service selection
- **Security scanning** that identifies security vulnerabilities in your code and suggests fixes, with awareness of AWS IAM policy issues, exposed credentials, and injection vulnerabilities
- **Infrastructure as Code assistance** for CloudFormation, AWS CDK (all languages), Terraform (AWS provider), and AWS SAM templates
- **AWS service integration guidance** — Q Developer is trained on AWS documentation, SDKs, and service best practices, making it uniquely effective for AWS-native development
- **Transformation** capabilities for Java and .NET modernization (Java upgrade automation, .NET to Linux migration)

AWS Q Developer is the best tool in the FORGE toolkit for:
- Teams whose target platform is AWS (ECS, EKS, Lambda, RDS, SQS, SNS, DynamoDB, etc.)
- Cloud migration projects targeting AWS
- Infrastructure-as-code development for AWS infrastructure
- AWS security posture improvement (IAM policy generation and review, Security Hub finding remediation)

---

## Prerequisites

- [ ] An AWS account or AWS IAM Identity Center access
- [ ] VS Code 1.85 or later, OR IntelliJ IDEA / other JetBrains IDE 2023.2 or later
- [ ] For Pro tier features (larger context, additional models): AWS Q Developer Pro subscription (available through AWS console or your AWS account team)
- [ ] For teams: AWS IAM Identity Center configured for your organization (recommended for enterprise deployments)
- [ ] Familiarity with the AWS services targeted by your project

### License Tiers

| Tier | Cost | Key Features | Best For |
|---|---|---|---|
| Q Developer Free | Free with AWS Builder ID | Code completion, basic chat, security scanning (10 files/month) | Individual developers, experimentation |
| Q Developer Pro | $19/user/month | Full security scanning, longer context, admin controls, Q Transform | Enterprise teams, production projects |

For enterprise FORGE deployments, Q Developer Pro is recommended — it includes admin-managed workspace configurations and audit logging.

---

## Installation: VS Code

### Step 1: Install the Extension

1. Open VS Code Extensions panel (Ctrl+Shift+X / Cmd+Shift+X)
2. Search for `AWS Toolkit`
3. Install **AWS Toolkit** (publisher: Amazon Web Services)
   - This extension bundle includes AWS Q Developer, CloudFormation linting, SAM CLI integration, S3 browser, CloudWatch Logs viewer, and more
   - AWS Q Developer functionality is activated within the AWS Toolkit

Alternatively, install just the Q Developer extension:
1. Search for `Amazon Q`
2. Install **Amazon Q** (publisher: Amazon Web Services)

### Step 2: Authenticate

#### Option A: AWS Builder ID (Free tier — individual)
1. Click the AWS icon in the VS Code Activity Bar
2. Select `Sign in to AWS Builder ID`
3. Follow the browser-based OAuth flow
4. No AWS account required for the free tier

#### Option B: IAM Identity Center (Pro tier — enterprise)
1. Click the AWS icon in the VS Code Activity Bar
2. Select `Sign in with IAM Identity Center`
3. Enter your organization's IAM Identity Center Start URL (format: `https://your-org.awsapps.com/start`)
4. Follow the browser-based OAuth flow
5. Select the AWS account and permission set granted by your AWS administrator

#### Option C: IAM User Credentials (Legacy — not recommended for new setups)
```bash
aws configure
# Enter Access Key ID, Secret Access Key, region, output format
```

### Step 3: Verify Q Developer is Active

1. Open a source file (e.g., a Java or Python file)
2. Start typing code — you should see inline suggestions appearing in gray
3. Press Tab to accept a suggestion
4. Open Q Developer Chat: View → Q Developer Chat (or click the Q icon in the Activity Bar)

---

## Installation: JetBrains IDEs

### Step 1: Install the Plugin

1. Open IntelliJ IDEA (or PyCharm, GoLand, etc.)
2. Settings → Plugins → Marketplace
3. Search for `AWS Toolkit`
4. Install **AWS Toolkit** (publisher: Amazon Web Services)
5. Restart the IDE

### Step 2: Authenticate

1. After restart, click the AWS icon in the right-side tool panel
2. Select `Connect to AWS` or `Sign in with IAM Identity Center`
3. Follow the same authentication steps as VS Code Option A or B above

---

## Configuring Workspace Instructions for FORGE

AWS Q Developer Pro supports workspace-level custom instructions that persist across sessions and are applied to all Q Developer Chat interactions within a project.

### Method 1: Q Developer Workspace Instructions (Pro Tier)

1. In VS Code, open Q Developer Chat
2. Click the settings gear icon in the Q Developer panel
3. Select `Workspace Instructions`
4. Add your FORGE project instructions (see template below)
5. Click Save — these instructions are now applied to every chat message in this workspace

**FORGE Workspace Instructions Template for Q Developer**:

```
You are assisting with [PROJECT NAME] under the FORGE framework (Framework for Orchestrated AI-Guided Engineering).

PROJECT TYPE: [Greenfield | Cloud Migration | AWS Modernization | API Development]
PRIMARY LANGUAGE: [Java 17 | Python 3.12 | TypeScript | Go]
AWS TARGET SERVICES: [e.g., ECS Fargate, RDS PostgreSQL, SQS, SNS, API Gateway, Lambda]
INFRASTRUCTURE AS CODE: [Terraform | AWS CDK | CloudFormation | SAM]

FORGE RULES — ALWAYS APPLY:
1. Never invent AWS service configurations or IAM policies that are not validated. Always cite the AWS documentation or service limits for configuration values.
2. All IAM policies must follow least-privilege principle. Never generate wildcard resource ARNs (*) unless explicitly required and justified.
3. Never hardcode AWS credentials, access keys, or secrets in code. Use IAM roles, AWS Secrets Manager, or AWS Systems Manager Parameter Store.
4. Generate infrastructure as code alongside application code — never describe infrastructure in prose without providing the IaC.
5. All S3 bucket policies must block public access by default.
6. All RDS/Aurora instances must have encryption at rest enabled.
7. All Lambda functions must have appropriate VPC configuration if accessing VPC resources.
8. Tag all AWS resources with: Project, Environment, Owner, CostCenter — use the project's tagging standard.
9. Never suggest deprecated AWS services or APIs. Use current service equivalents.
10. Flag any AWS service limits that may affect the design (e.g., Lambda concurrency limits, SQS message size, DynamoDB item size).

ARCHITECTURAL PATTERNS:
[USER FILLS — describe your AWS architecture patterns]

WHAT NOT TO SUGGEST:
[USER FILLS — services or patterns that have been rejected in ADRs]
```

### Method 2: Q Developer System Prompt via AWS Console (Enterprise Admin)

For enterprise deployments with Q Developer Pro and AWS Organizations:

1. Log in to the AWS Console with an admin account
2. Navigate to: Amazon Q → Settings → Workspace Settings
3. Configure organization-wide default instructions
4. These are applied to all users in the organization's Q Developer deployment

### Method 3: `.qdeveloper` Directory (emerging — check current docs)

AWS is developing file-based context loading similar to `.cursorrules`. Check the current AWS Q Developer documentation for whether a project-scoped instruction file is supported in your version.

---

## When to Use AWS Q Developer vs. Other FORGE Tools

Use this decision table to choose the right tool for each task in an AWS-focused project.

| Task | Best Tool | Why |
|---|---|---|
| AWS CDK code generation | **AWS Q Developer** | Deep CDK construct knowledge, AWS service defaults |
| CloudFormation template authoring | **AWS Q Developer** | Knows CF resource types, properties, and constraints |
| Terraform for AWS | **AWS Q Developer** | Knows AWS Terraform provider resource types |
| IAM policy generation | **AWS Q Developer** | Knows IAM actions, resource ARN formats, condition keys |
| Lambda function implementation | **AWS Q Developer** + GitHub Copilot | Q for AWS SDK; Copilot for business logic |
| ECS task definition and service config | **AWS Q Developer** | Knows ECS configuration parameters and best practices |
| Security vulnerability scanning | **AWS Q Developer** | Q security scan + SAST integration |
| Architecture design and ADRs | **Claude Code** | Better multi-file reasoning, structured ADR output |
| COBOL legacy analysis | **watsonx Code Assist for Z** | Purpose-built for mainframe |
| Java business logic implementation | **GitHub Copilot** or **Claude Code** | Better general Java code quality |
| Multi-file code review | **Claude Code** | Better large-context analysis |
| Greenfield scaffolding | **Cursor** | Better full-project generation |
| AWS documentation lookup | **AWS Q Developer** | Trained on AWS docs, current service information |
| Security Hub finding remediation | **AWS Q Developer** | Knows Security Hub finding formats and AWS remediations |

---

## FORGE Workflows for AWS Q Developer

### Workflow 1: Cloud Migration Assessment

Use Q Developer Chat to assess an existing application for AWS migration readiness:

```
Assess the following [Java/Python/Node.js] application for migration to AWS.

Application description: [describe what it does]
Current runtime: [on-premises / another cloud]
Key characteristics:
- Languages: [list]
- Database: [type and version]
- Messaging: [if applicable]
- External integrations: [list]
- Estimated load: [requests/sec, data volume]

Provide:
1. Recommended AWS migration strategy: REHOST | REPLATFORM | REFACTOR | RETIRE | RETAIN
2. Target AWS architecture diagram (describe as a component list with connections)
3. Recommended AWS services for each component with justification
4. Migration risks and mitigations
5. Estimated AWS monthly cost range (ballpark)
6. Migration sequencing recommendation (what to migrate first)

Apply FORGE rules: separate facts from assumptions; flag open questions.
```

### Workflow 2: AWS CDK Stack Generation

```
Generate an AWS CDK v2 TypeScript stack for the following infrastructure:

Requirements:
[USER FILLS — describe what the infrastructure needs to do]

Constraints:
- All resources must be tagged: Project=[NAME], Environment=[ENV], Owner=[TEAM]
- All S3 buckets: block public access, versioning enabled, server-side encryption (SSE-S3 or SSE-KMS)
- All RDS instances: Multi-AZ, encryption at rest, automated backups 7-day retention
- All ECS services: Fargate launch type, no EC2 instances
- VPC: 3 availability zones, private and public subnets, NAT Gateway in each AZ
- No hardcoded account IDs, region names, or ARNs — use CDK context or SSM parameters
- IAM roles: least-privilege, no * resource ARNs unless justified with a comment

Apply FORGE rules: generate unit tests for the CDK stack using aws-cdk-lib/assertions.
```

### Workflow 3: IAM Policy Generation

```
Generate a least-privilege IAM policy for the following use case:

Service: [e.g., ECS task running a Java application]
Resources it needs to access:
- [e.g., S3 bucket 'my-app-bucket' — read and write to prefix 'uploads/']
- [e.g., SQS queue 'my-app-queue' — send and receive messages]
- [e.g., Secrets Manager secret 'my-app/db-password' — read only]
- [e.g., DynamoDB table 'my-app-table' — read and write items]

Provide:
1. IAM policy document in JSON format
2. For each permission statement: why this permission is required
3. Any conditions that should be added (e.g., source IP, MFA, VPC endpoint)
4. Any permissions you would normally include but are excluding due to least-privilege
5. AWS CDK code to attach this policy to an IAM role

Flag any permissions where you are uncertain about the exact action name — mark those with [VERIFY: check AWS docs for exact action].
```

### Workflow 4: Security Finding Remediation

```
The following AWS Security Hub finding was raised for my application:

Finding: [paste the Security Hub finding JSON or description]

Provide:
1. Plain-English explanation of what the security risk is
2. Step-by-step remediation steps
3. Infrastructure as code change ([Terraform | CDK | CloudFormation]) to fix the configuration
4. Application code change if applicable
5. How to verify the finding is resolved after remediation

Apply FORGE rules: do not suggest disabling security controls as a fix.
```

### Workflow 5: Q Developer Security Scan

AWS Q Developer Pro includes automated security scanning. To run a security scan:

1. Open Q Developer Chat
2. Type: `/review` or click the Security Scan button
3. Q Developer will scan the currently open file or selected code
4. Review findings categorized by: Critical, High, Medium, Low
5. For each finding, Q Developer can suggest a fix — review the suggestion before applying

**FORGE integration**: After running Q Developer's security scan, use this prompt to prioritize findings:

```
Q Developer has produced the following security scan findings for [file/project]:
[paste findings]

As a FORGE security review, prioritize these findings:
1. Which findings are BLOCKERS that must be fixed before any deployment?
2. Which are HIGH priority (fix within this sprint)?
3. Which are MEDIUM priority (fix within this release)?
4. For each Critical and High finding, provide the specific code change needed.

Apply FORGE rules: do not suggest disabling security checks or suppressing findings without justification.
```

---

## AWS Q Developer Transform (Java and .NET)

For Java version upgrades (e.g., Java 8 → Java 17) or .NET to Linux migrations, Q Developer includes a transformation service:

### Java Upgrade with Q Transform

1. In VS Code with AWS Toolkit: Open Q Developer Chat
2. Type: `/transform`
3. Select: `Java upgrade`
4. Choose source version (e.g., Java 8 or 11) and target version (Java 17 or 21)
5. Q Transform will analyze your Maven/Gradle project and apply automated upgrades

**FORGE integration**: After Q Transform runs, use this review prompt:

```
Q Developer Transform has upgraded this Java project from [SOURCE] to [TARGET].
Review the transformed code and:
1. Identify any behavioral changes introduced by the upgrade (especially around deprecated API replacements)
2. Flag any changes that may affect distributed behavior (serialization, reflection, security policies)
3. Identify any areas where the transformation was incomplete (// TODO comments, manually-fix-required notes)
4. Verify that the FORGE architectural patterns are still intact after transformation

Apply FORGE Rule 2: list all behavioral changes explicitly.
```

---

## AWS-Specific FORGE Guardrails

These guardrails supplement the main FORGE Constitution for AWS workloads:

1. **Never expose public S3 buckets**: All S3 buckets must have `BlockPublicAcls: true`, `BlockPublicPolicy: true`, `IgnorePublicAcls: true`, `RestrictPublicBuckets: true` unless the specific use case requires public access (e.g., static website hosting) and it is explicitly approved.

2. **Never use root account credentials**: All programmatic access must use IAM roles or IAM Identity Center. Never generate code that uses root account access keys.

3. **Always encrypt sensitive data at rest**: RDS, DynamoDB (at-rest encryption), S3 (SSE), EBS (encrypted volumes), SQS (SSE-SQS or SSE-KMS) where sensitive data is stored.

4. **Always use VPC endpoints for service access**: When accessing S3, Secrets Manager, SSM, or other AWS services from within a VPC, use VPC Gateway or Interface endpoints to avoid traffic transiting the public internet.

5. **Tag everything**: Every AWS resource must have the project's standard tags. Generate tagging in all IaC. Untagged resources are a cost management and compliance risk.

6. **Avoid data transfer costs**: Be aware of cross-AZ and cross-region data transfer costs when designing AWS architectures. Same-AZ communication is free; cross-AZ is not.

7. **Use managed services where appropriate**: Prefer AWS managed services (RDS over EC2+database, ECS/Fargate over EC2+Docker, Elasticache over EC2+Redis) to reduce operational burden. Document reasons for not using a managed service in an ADR.
