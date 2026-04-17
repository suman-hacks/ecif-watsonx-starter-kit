# FORGE Persona: Business Analyst

## Who Is This?

Business Analysts bridge the gap between business operations and technology delivery. They are responsible for understanding how the business actually works today, documenting those processes and rules precisely, and translating them into requirements that development teams can act on. In enterprise modernization projects, the BA is often the person who sits with SMEs (subject matter experts), reads legacy COBOL code, interprets decades-old process documents, and turns ambiguous institutional knowledge into structured, testable requirements.

The BA does not own the solution — that belongs to the Solution Architect and developers. But the BA owns the problem space: what the system does today, what the business needs it to do tomorrow, and what gaps exist between those two states.

---

## What AI Can Help a Business Analyst Do

FORGE dramatically accelerates the most labor-intensive parts of BA work:

- **Extract business rules** from legacy code (COBOL, RPG, PL/I, old Java) or dense documentation in minutes, not days
- **Document process flows** in structured BPMN-style notation without drawing tools
- **Map as-is to to-be** processes with a structured gap analysis framework
- **Generate data dictionaries** from database schemas, code, or flat file layouts — turning technical field definitions into business-readable documentation
- **Produce gap analyses** that prioritize gaps by business impact and effort
- **Draft BRDs** that cover business objectives, functional requirements, and traceability
- **Create stakeholder requirements matrices** to validate coverage and avoid missing requirements

---

## SDLC Stages This Persona Participates In

| Stage | Role |
|-------|------|
| **Discovery** | Primary — leads stakeholder interviews, process walkthroughs, legacy analysis |
| **Requirements** | Primary — authors BRD, business rules catalog, data dictionary, process flows |
| **Architecture** | Contributor — provides business context and constraints to architects; validates that design covers requirements |
| **Build** | Support — answers business questions, clarifies edge cases, validates that implementation follows business rules |
| **Testing** | Reviewer — reviews QA test cases for business rule coverage; identifies gaps in test scenarios |
| **UAT** | Support — assists UAT team in understanding business scenarios; validates test scripts against BRD |

---

## How to Use FORGE as a Business Analyst

1. **Start with discovery materials.** The Business Rule Extraction and As-Is/To-Be prompts need real input — legacy code, process descriptions, or interview notes. The richer your input, the more accurate the output.
2. **Use the Data Dictionary early.** A shared data dictionary prevents misunderstandings throughout the project. Generate it as soon as you have access to source code or database schemas.
3. **Chain your outputs.** Your business rules feed the Architect's service decomposition and the Developer's implementation. Your data dictionary feeds the QA team's test data generation. Your gap analysis feeds the Project Manager's risk register.
4. **Validate, don't just generate.** AI output for business rules must be validated with domain SMEs. Mark every extracted rule with a confidence level and get sign-off before handing to development.

---

## Prompts in This Pack

| # | Prompt Name | When to Use |
|---|-------------|-------------|
| 1 | Business Rule Extraction | Analyzing legacy systems or documentation to extract rules |
| 2 | Process Flow Documentation | Documenting current or future business processes |
| 3 | As-Is to To-Be Mapping | Capturing process transformation scope |
| 4 | Data Dictionary Creation | Creating business-readable field definitions from code/schema |
| 5 | Gap Analysis | Identifying what's missing between current and future state |
| 6 | BRD (Business Requirements Document) | Producing a formal requirements document |
| 7 | Stakeholder Requirements Matrix | Validating that all stakeholder needs are covered |

---

## Outputs This Persona Produces

- Business Rules Catalog (structured, with Rule IDs)
- Process flow documents (BPMN-style text notation)
- As-Is / To-Be process comparison
- Data dictionary (business-readable)
- Gap analysis with priority and effort ratings
- Business Requirements Document (BRD)
- Stakeholder Requirements Matrix

---

## Key Handoffs

| Output | Handed To | Used For |
|--------|-----------|---------|
| Business Rules Catalog | Developer | Business logic implementation |
| Business Rules Catalog | QA Engineer | Test case generation for business rules |
| Process Flows | Solution Architect | Service boundary and API design |
| Data Dictionary | Developer | Field naming and validation implementation |
| Data Dictionary | QA Engineer | Test data generation |
| Gap Analysis | Solution Architect | Migration scoping and phasing |
| Gap Analysis | Project Manager | Risk register and project planning |
| BRD | Product Owner | Epic and user story creation |
| Stakeholder Requirements Matrix | All | Requirements traceability |

---

*FORGE — Framework for Orchestrated AI-Guided Engineering*
