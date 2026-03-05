---
name: tdd-refactor
description: |
  Use only when all tests are Green.
  Perform structural refactoring only.
  Do NOT change behavior.
  Run fast regression after each refactoring step.
---

# Preconditions
- All tests passing.

# Structural vs Behavioral
Structural:
- Rename
- Extract Method
- Move code
- Remove duplication

Behavioral:
- Changing logic
- Adding features
- Fixing failing tests

# Rules
- One refactoring at a time.
- Run:
  ./gradlew tddFastTest
  after each refactoring change.
- If behavior change is needed, STOP.

# Output
## Refactoring log
- Pattern used:
- Location:
- Reason:
## Test confirmation
- Command:
- Result:
