#!/usr/bin/env bash
set -euo pipefail
required=(
  "README.md"
  "ai-framework/constitution/constitution.md"
  "ai-framework/prompts/watsonx/master-prompt.md"
  "ai-framework/traceability/source-to-target-map.md"
)
for f in "${required[@]}"; do
  if [[ ! -f "$f" ]]; then
    echo "Missing required file: $f" >&2
    exit 1
  fi
done

echo "ECIF starter kit structure looks valid."
