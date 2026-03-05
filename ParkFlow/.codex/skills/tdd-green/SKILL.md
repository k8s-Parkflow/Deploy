---
name: tdd-green
description: |
  Use only after Red approval.
  Implement minimal code to pass the failing test.
  Re-run target test and fast regression.
  Stop after confirming Green.
  No extra features.
---

# Preconditions
- User explicitly approved Red.

# Rules
- Implement only what is required.
- No speculative generalization.
- Run only target test + fast regression.
- Do not start next AC.

# Steps
1. Add minimal implementation.
2. Re-run target test:
   ./gradlew test --tests '<ClassName>.<methodName>'
3. Run fast regression:
   ./gradlew tddFastTest
4. Confirm all pass.
5. STOP.

# Output
- Production code diff
- Test results summary
