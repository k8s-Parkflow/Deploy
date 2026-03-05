---
name: commit
description: |
  Use when preparing to commit.
  Validate commit meets Tidy First rules.
  Block commit if rules are violated.
---

# Commit Rules
1. Local merge gate passed.
2. Single logical unit of work.
3. Commit is exactly one type:
   - structural (refactor only)
   - behavioral (feat/fix only)
4. Structural and behavioral changes are not mixed.
5. Commit message should be always Eng (not Kor)

# Steps
1. Run local gate:
   ./.codex/skills/local-merge-gate/scripts/run_local_gate.sh
2. Review diff.
3. Confirm commit type.
4. Generate commit message template.

# Output
## Commit type
- structural / behavioral

## Suggested message
- structural: refactor: short summary
- behavioral: feat|fix: short summary

## Validation
- Gate: PASS/FAIL
- Mixed changes: YES/NO
