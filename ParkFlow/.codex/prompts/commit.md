---
description: Validate local merge gate and prepare disciplined commit message
argument-hint: [TYPE=structural|behavioral]
---

커밋 준비 점검을 수행한다.

입력:
- TYPE=$TYPE (선택). 없으면 diff를 보고 스스로 분류한다.
- 추가 지시: $ARGUMENTS

절차:
1. 로컬 머지 게이트 실행:
   `./.codex/skills/local-merge-gate/scripts/run_local_gate.sh`
2. `git status --short`와 `git diff --stat`으로 변경 범위를 요약한다.
3. 변경을 `structural` 또는 `behavioral` 중 하나로 분류한다.
4. 두 성격이 섞여 있으면 커밋을 차단하고 분리 커밋 계획을 제시한다.
5. 통과 시 커밋 메시지 초안을 제시한다.
   - structural: `refactor: ...`
   - behavioral: `feat: ...` 또는 `fix: ...`

규칙:
- 증거 없이 PASS를 선언하지 않는다.
- 테스트/게이트 실패 시 원인 파일과 실패 요약을 먼저 제시한다.
- 사용자가 명시적으로 요청한 경우에만 `git add`/`git commit`을 실행한다.
