#!/usr/bin/env bash
set -euo pipefail
NAME="ecif-watsonx-starter-kit"
VERSION="0.1.0"
OUT="${NAME}-${VERSION}.zip"
zip -r "$OUT" . -x "*.DS_Store" > /dev/null
printf 'Created %s\n' "$OUT"
