---
name: feature-to-ac
description: |
  Use when the user describes a new feature or behavior.
  Convert the feature into small, testable Acceptance Criteria (AC) for TDD.
  Select exactly one AC for the next Red step.
  Do not modify code.
---

# Goal
Break one feature into behavior-first, testable increments.

# Steps
1. Restate feature in user-visible behavior.
2. Create 3-7 AC items:
   - Each AC must be testable with a single failing test.
   - Include at least one failure/edge case AC.
3. Write each AC in Given/When/Then style.
4. Select exactly one AC as the next increment.
5. STOP.

# Output format
## AC List
- [ ] AC-1: Given ... When ... Then ...
- [ ] AC-2: Given ... When ... Then ...

## Next AC
- Selected: ...
- Reason: ...
