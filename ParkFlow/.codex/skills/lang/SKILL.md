---
name: lang
description: |
  Apply Korean as the default response language.
  Use only for language policy, not for coding workflow decisions.
---

# Trigger
Always use Korean for output.

# Rules
- Any generated text, code comments, explanations, logs must be in Korean.
- When embedding code examples, comments in code can be bilingual but surrounding explanation must be Korean.
- Do not output English explanations except in code examples where syntax requires English tokens.

# Output
- Korean natural language explanations.
- Korean documentation strings.
