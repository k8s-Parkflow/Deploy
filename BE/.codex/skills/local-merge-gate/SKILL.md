---
name: local-merge-gate
description: |
  Enforce a local merge gate for this Gradle/Spring project when CI is absent.
  Use before merge after TDD Red/Green/Refactor is complete.
  Execute integration tests, acceptance tests, and coverage verification with project-supported commands only.
  Block merge on any failure.
---

# Preconditions
- Run from repository root.
- Use only commands that exist in this project.
- Stop immediately when any command fails.
- TDD loop itself is handled by: `tdd-red`, `tdd-green`, `tdd-refactor`.

# Local Merge Gate (mandatory)
Run in order. Any failure blocks merge.

1) Integration tests
   ./gradlew integrationTest

2) Acceptance tests
   ./gradlew acceptanceTest

3) Coverage verification
   ./gradlew clean test jacocoTestCoverageVerification

# One-command execution
- Preferred:
  ./.codex/skills/local-merge-gate/scripts/run_local_gate.sh

# Output
## Gate Results
- Integration:
- Acceptance:
- Coverage:

## Decision
- ALLOW / BLOCK
- Reason:
