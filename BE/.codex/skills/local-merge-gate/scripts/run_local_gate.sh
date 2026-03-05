#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/../../../.." && pwd)"
cd "$ROOT_DIR"

run_step() {
  local label="$1"
  shift
  echo "[RUN] $label"
  if "$@"; then
    echo "[PASS] $label"
  else
    echo "[FAIL] $label"
    return 1
  fi
}

run_step "Integration" ./gradlew integrationTest
run_step "Acceptance" ./gradlew acceptanceTest
run_step "Coverage" ./gradlew clean test jacocoTestCoverageVerification

cat <<'EOF'
## Gate Results
- Integration: PASS
- Acceptance: PASS
- Coverage: PASS

## Decision
- ALLOW
- Reason: All required local gate checks passed.
EOF
