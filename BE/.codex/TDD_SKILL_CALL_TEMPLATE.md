# TDD Skill Call Template

이 문서는 이 프로젝트에서 스킬 호출을 일관되게 하기 위한 템플릿입니다.
아래 문장을 그대로 복사해도 되고, `<...>`만 바꿔서 사용해도 됩니다.

## 0) 언어 정책(선택)
`$lang 앞으로 모든 설명은 한국어로 진행해줘.`

## 1) 기능을 AC로 쪼개기
`$feature-to-ac 기능: <사용자 기능 설명>. 테스트 가능한 AC로 3~7개로 쪼개고 다음 Red로 갈 AC 하나만 선택해줘.`

## 2) Red 단계(승인 전)
`$tdd-red 선택된 AC: <AC-번호/내용>. 실패하는 테스트 1개만 추가하고 해당 테스트만 실행해서 실패 로그 요약 후 멈춰줘.`

## 3) Red 승인
`Red 승인. Green으로 진행해줘.`

## 4) Green 단계(최소 구현)
`$tdd-green 방금 Red 테스트를 통과시키는 최소 구현만 적용해줘. 대상 테스트 + tddFastTest 실행 결과를 보여주고 멈춰줘.`

## 5) Refactor 단계(Tidy First)
`$tdd-refactor 현재 Green 상태에서 구조 개선만 1단계 진행해줘. 동작 변경 없이 tddFastTest로 확인해줘.`

## 6) 로컬 머지 게이트
`$local-merge-gate 로컬 머지 게이트를 실행해줘.`

또는 스크립트 직접 실행:
`./.codex/skills/local-merge-gate/scripts/run_local_gate.sh`

## 7) 커밋 준비 점검
`$commit 현재 변경이 구조/행동 분리 원칙을 지키는지 검증하고 커밋 메시지 템플릿을 제안해줘.`

## 8) 다음 AC 반복
`다음 AC로 같은 흐름(2~7단계)을 반복하자.`

---

## 빠른 체크리스트
- 한 번에 AC 하나만 진행한다.
- Red에서는 테스트만 추가하고 구현은 금지한다.
- Green에서는 최소 구현만 허용한다.
- Refactor에서는 구조 변경만 허용한다.
- 머지 전에는 local-merge-gate를 반드시 통과한다.
- 커밋은 structural / behavioral를 섞지 않는다.
