#!/usr/bin/env bash
set -euo pipefail

MODULE_NAME="${1:-sample-module}"
ROOT="modernization-work/${MODULE_NAME}"

mkdir -p "${ROOT}/source-artifacts" "${ROOT}/analysis-output" "${ROOT}/target-design" "${ROOT}/generated-code"

cat > "${ROOT}/analysis-output/README.md" <<'EOF'
Store legacy understanding outputs here.
EOF

cat > "${ROOT}/target-design/README.md" <<'EOF'
Store service boundaries, API contracts, events, and mapping decisions here.
EOF

cat > "${ROOT}/generated-code/README.md" <<'EOF'
Store generated code, tests, and hardening notes here.
EOF

echo "Created payments modernization workspace at ${ROOT}"
