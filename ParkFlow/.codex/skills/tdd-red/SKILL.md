---
name: tdd-red
description: |
  Use after one AC is selected.
  Write exactly one failing test.
  Run only the target test and confirm failure.
  Stop and wait for explicit user approval.
  Do not implement production code.
---

# Preconditions
- One AC is explicitly selected.

# Rules
- Write ONE test only.
- No production logic to pass the test.
- Test must fail clearly.
- Run target test only.

# Steps
1. Write failing test.
2. Execute:
   ./gradlew test --tests '<ClassName>.<methodName>'
3. Show failure summary.
4. STOP and ask for approval.

# Output
- Test diff
- Failure output summary
- Approval request
