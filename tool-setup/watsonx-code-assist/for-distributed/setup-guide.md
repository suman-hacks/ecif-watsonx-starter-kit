# IBM watsonx Code Assist (Distributed) and IBM watsonx.ai / BAM — Setup and FORGE Integration Guide

> This guide covers IBM watsonx Code Assist for distributed (non-mainframe) workloads and IBM watsonx.ai / BAM (the API-based platform for IBM foundation models). These are distinct from watsonx Code Assist for Z — they target Java, Python, Go, JavaScript, and other distributed languages, not COBOL/z/OS.

---

## What is watsonx Code Assist (Distributed)?

IBM watsonx Code Assist (distributed edition) is IBM's AI coding assistant for modern distributed languages. It integrates with VS Code and JetBrains IDEs and provides:

- Code completion and generation for Java, Python, Go, JavaScript, TypeScript, C/C++, Rust, and others
- Inline explanations and code documentation generation
- Unit test generation
- Code review and refactoring suggestions
- Natural language-to-code generation via chat interface

Unlike watsonx Code Assist for Z, the distributed edition is not specialized for COBOL or z/OS. It is a general-purpose distributed coding assistant backed by IBM's Granite family of foundation models, which are specifically trained on enterprise code and documentation.

**When to use over GitHub Copilot or Claude Code**:
- Your organization has an IBM enterprise agreement that includes watsonx Code Assist
- Your organization requires AI-generated code to come exclusively from IBM-governed, IBM-hosted models (data residency, enterprise indemnification)
- You are working in a mixed IBM stack environment (Liberty, WebSphere, Db2, OpenShift) where IBM-specific framework knowledge is valuable
- Your compliance posture requires that no code or data leaves IBM Cloud infrastructure

---

## What is IBM watsonx.ai / BAM?

IBM watsonx.ai is IBM's enterprise AI platform, available at https://dataplatform.cloud.ibm.com. IBM BAM (IBM Research's Big AI Model service) is the research-track predecessor to watsonx.ai, available at https://bam.res.ibm.com. Both provide REST API access to IBM's Granite foundation models and other partner models.

**When to use the API (watsonx.ai / BAM) over the IDE tool**:
- You need to analyze hundreds of source files programmatically (batch analysis pipeline)
- You are building an automated modernization toolchain that calls an AI model as a service
- You want to fine-tune a Granite model on your organization's proprietary codebase or documentation
- You are building a custom application that incorporates AI-generated content
- You need to run controlled experiments comparing different model behaviors

---

## Differences from watsonx Code Assist for Z

| Feature | watsonx Code Assist for Z | watsonx Code Assist (Distributed) |
|---|---|---|
| Target languages | COBOL, PL/1, JCL, REXX, Assembler | Java, Python, Go, JavaScript, TypeScript, C/C++ |
| z/OS constructs | Yes (CICS, IMS, DB2 for Z, VSAM) | No |
| IBM framework awareness | z/OS specific | WebSphere, Liberty, OpenShift, Db2 LUW, MQ |
| IDE integration | VS Code + IBM Z Open Editor | VS Code, IntelliJ/JetBrains |
| API access | No | Yes (via watsonx.ai) |
| Best for | Legacy mainframe analysis | Enterprise Java/Python development |

---

## Prerequisites

### IBM watsonx Code Assist (VS Code Extension)

- [ ] VS Code 1.85 or later, or IntelliJ IDEA 2023.2 or later
- [ ] An active IBM watsonx Code Assist license (contact IBM account team)
- [ ] Access to IBM Software Hub or IBM Entitlement Registry to download the extension `.vsix`
- [ ] An IBM Cloud account or on-premises watsonx deployment (depending on your organization's contract)

### IBM watsonx.ai API Access

- [ ] An IBM Cloud account (https://cloud.ibm.com)
- [ ] An IBM watsonx.ai service instance provisioned in your IBM Cloud account
- [ ] An IBM Cloud API key with IAM permissions to invoke the watsonx.ai service
- [ ] The watsonx.ai project ID for your service instance (visible in the watsonx.ai console)

### IBM BAM (Research Track)

- [ ] An IBM Research BAM account (request at https://bam.res.ibm.com — IBM employees and approved research partners only)
- [ ] A BAM API key

---

## Installation: VS Code Extension

### Step 1: Obtain the Extension

The watsonx Code Assist extension for distributed workloads is available from:
- IBM Software Hub (for enterprise license holders)
- IBM Passport Advantage
- Some releases are available on the VS Code Marketplace — search for `IBM watsonx Code Assist`

Install from VSIX:
1. In VS Code: Extensions panel → `...` menu → `Install from VSIX`
2. Select the downloaded `.vsix` file
3. Reload VS Code when prompted

### Step 2: Authenticate

1. Open VS Code Command Palette (Ctrl+Shift+P)
2. Run: `watsonx Code Assist: Sign In`
3. Choose your authentication method:
   - **IBM Cloud (SaaS)**: Sign in with your IBM Cloud API key
   - **On-premises deployment**: Enter your organization's watsonx URL and API key

### Step 3: Configure the Extension

Open VS Code Settings (Ctrl+,) and search for `watsonx`. Configure:

- `watsonx.apiUrl`: Your watsonx.ai API endpoint (e.g., `https://us-south.ml.cloud.ibm.com`)
- `watsonx.projectId`: Your watsonx.ai project ID
- `watsonx.modelId`: The model to use (e.g., `ibm/granite-20b-code-instruct` or `ibm/granite-34b-code-instruct`)
- `watsonx.maxTokens`: Maximum tokens per completion (recommend 2048 for code generation)
- `watsonx.temperature`: Temperature for code generation (recommend 0.1–0.3 for deterministic outputs)

---

## Installation: watsonx.ai API (Programmatic Access)

### Step 1: Provision a Service Instance

1. Log in to https://cloud.ibm.com
2. Navigate to Catalog → AI / Machine Learning → watsonx.ai
3. Create a service instance in your preferred region (Dallas, Frankfurt, Tokyo, London)
4. Note the service instance URL and your project ID from the watsonx.ai console

### Step 2: Generate an API Key

1. In IBM Cloud: Manage → Access (IAM) → API keys
2. Create a new API key. Store it in your organization's secrets manager (AWS Secrets Manager, IBM Secrets Manager, HashiCorp Vault)
3. Never commit the API key to source control

### Step 3: Install the Python SDK

```bash
pip install ibm-watsonx-ai
```

Or for Node.js:
```bash
npm install @ibm-cloud/watsonx-ai
```

### Step 4: Test the Connection

**Python**:
```python
from ibm_watsonx_ai import APIClient, Credentials
from ibm_watsonx_ai.foundation_models import ModelInference
from ibm_watsonx_ai.foundation_models.utils.enums import ModelTypes

credentials = Credentials(
    url="https://us-south.ml.cloud.ibm.com",
    api_key="YOUR_IBM_CLOUD_API_KEY"   # retrieve from secrets manager, not hardcoded
)

model = ModelInference(
    model_id=ModelTypes.GRANITE_20B_CODE_INSTRUCT,
    credentials=credentials,
    project_id="YOUR_PROJECT_ID",
    params={
        "max_new_tokens": 2048,
        "temperature": 0.1,
        "repetition_penalty": 1.1,
        "stop_sequences": ["```"]
    }
)

response = model.generate_text(
    prompt="// Java function to validate a credit card PAN using Luhn algorithm\npublic boolean"
)
print(response)
```

**Node.js**:
```javascript
import WatsonxAiMlVml_v1 from '@ibm-cloud/watsonx-ai';
import { IamAuthenticator } from 'ibm-cloud-sdk-core';

const client = WatsonxAiMlVml_v1.newInstance({
  authenticator: new IamAuthenticator({ apikey: process.env.IBM_CLOUD_API_KEY }),
  serviceUrl: 'https://us-south.ml.cloud.ibm.com',
  version: '2024-05-31',
});

const params = {
  input: '// Java function to validate a credit card PAN\npublic boolean',
  modelId: 'ibm/granite-20b-code-instruct',
  projectId: process.env.WATSONX_PROJECT_ID,
  parameters: { max_new_tokens: 512, temperature: 0.1 },
};

const result = await client.generateText(params);
console.log(result.result.results[0].generated_text);
```

---

## Using watsonx.ai with FORGE Prompts

### Loading the FORGE Constitution as a System Prompt

For API-based usage, always include the FORGE Constitution as the system prompt. Here is a production-ready system prompt template:

```python
FORGE_SYSTEM_PROMPT = """
You are an enterprise AI coding assistant operating under the FORGE framework 
(Framework for Orchestrated AI-Guided Engineering).

ALWAYS-ON RULES — apply to every response:
1. Never invent business logic not present in provided source artifacts or specifications.
2. Separate facts (from source), assumptions (explicitly stated), and recommendations (labeled as such).
3. Never include PII, credentials, PAN, CVV, or regulated data in outputs.
4. Generate tests alongside code — every non-trivial function must have corresponding tests.
5. Follow the provided architectural patterns exactly.
6. Flag ambiguity before generating code — ask focused questions.
7. All outputs must be human-reviewable and traceable to source artifacts.
8. Prefer incremental, reversible changes over big-bang transformations.
9. Flag security concerns immediately with [SECURITY CONCERN] notation.
10. Use project terminology consistently (see project context below).

PROJECT CONTEXT:
{project_context}
"""
```

Replace `{project_context}` with the content of your project's context file from `project-contexts/`.

### Batch Analysis Pattern

Use this pattern to analyze many files programmatically:

```python
import os
import json
from pathlib import Path
from ibm_watsonx_ai import APIClient, Credentials
from ibm_watsonx_ai.foundation_models import ModelInference
from ibm_watsonx_ai.foundation_models.utils.enums import ModelTypes

def analyze_source_file(model, source_code: str, filename: str) -> dict:
    """Analyze a source file using FORGE Stage 1 workflow."""
    
    prompt = f"""
Analyze the following source file as part of FORGE Stage 1 (Legacy Understanding).

File: {filename}

Provide:
1. Program purpose (2-3 sentences)
2. Key business rules (list, with source location)
3. External dependencies (databases, APIs, files, messaging)
4. Data structures (key entities and their fields)
5. Open questions requiring human clarification
6. Modernization risk: LOW | MEDIUM | HIGH with justification

Source code:
```
{source_code}
```

Format your response as JSON with keys: purpose, business_rules, dependencies, data_structures, open_questions, risk_level, risk_justification
"""
    
    response = model.generate_text(prompt=prompt)
    
    try:
        return json.loads(response)
    except json.JSONDecodeError:
        return {"raw_response": response, "parse_error": "Response was not valid JSON"}


def batch_analyze_directory(source_dir: str, output_dir: str, credentials, project_id: str):
    """Run FORGE Stage 1 analysis on all files in a directory."""
    
    model = ModelInference(
        model_id=ModelTypes.GRANITE_34B_CODE_INSTRUCT,
        credentials=credentials,
        project_id=project_id,
        params={"max_new_tokens": 4096, "temperature": 0.05}
    )
    
    source_path = Path(source_dir)
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)
    
    results = []
    
    for source_file in source_path.glob("**/*.java"):   # adjust extension as needed
        print(f"Analyzing: {source_file.name}")
        source_code = source_file.read_text(encoding='utf-8')
        
        analysis = analyze_source_file(model, source_code, source_file.name)
        
        output_file = output_path / f"{source_file.stem}-stage1-analysis.json"
        output_file.write_text(json.dumps(analysis, indent=2), encoding='utf-8')
        
        results.append({"file": str(source_file), "analysis": analysis})
    
    # Write summary
    summary_file = output_path / "batch-analysis-summary.json"
    summary_file.write_text(json.dumps(results, indent=2), encoding='utf-8')
    print(f"Analysis complete. {len(results)} files analyzed. Results in {output_dir}")
```

---

## Best Use Cases

| Use Case | Model Recommendation | Notes |
|---|---|---|
| Interactive code completion (IDE) | `granite-20b-code-instruct` | Faster, lower latency |
| Complex code generation and explanation | `granite-34b-code-instruct` | Higher quality for complex tasks |
| Large codebase analysis (batch) | `granite-34b-code-instruct` via API | Use async batch API for cost efficiency |
| Fine-tuning on org-specific code | Granite 8b or 13b (smaller models fine-tune better) | Requires IBM ML platform expertise |
| Multi-language analysis | `granite-34b-code-instruct` | Best cross-language understanding |
| Documentation generation | `granite-20b-code-instruct` | Efficient and cost-effective |

---

## FORGE Prompt Packs for watsonx.ai

The following FORGE prompt categories work particularly well with the IBM Granite code models via the API:

- **Stage 1 (Legacy Understanding)**: Large-context code analysis, business rule extraction
- **Stage 5 (Development)**: Code generation for Java/Spring Boot, Python/FastAPI
- **Stage 6 (Testing)**: Test case generation, test data synthesis
- **Batch mode**: Analyzing an entire application portfolio (hundreds of services) and producing a modernization inventory

For interactive architecture discussions and multi-step agentic workflows, Claude Code provides a better experience. For raw batch throughput across many files, watsonx.ai API is cost-effective and provides IBM data residency guarantees.

---

## Data Residency and Compliance

IBM watsonx.ai provides regional deployments to support data residency requirements:

| Region | IBM Cloud Region | Endpoint |
|---|---|---|
| United States | us-south (Dallas) | https://us-south.ml.cloud.ibm.com |
| Europe (GDPR) | eu-de (Frankfurt) | https://eu-de.ml.cloud.ibm.com |
| Asia Pacific | jp-tok (Tokyo) | https://jp-tok.ml.cloud.ibm.com |
| United Kingdom | eu-gb (London) | https://eu-gb.ml.cloud.ibm.com |

For organizations with strict data residency requirements:
- Select the region that matches your data residency obligation
- Confirm with IBM that your service tier includes the data residency commitment
- Do not include production data in prompts regardless of region — use synthetic or anonymized data
- Retain the FORGE prompt boundary policy (`guardrails/prompt-boundary-policy.md`) as your data classification guide
