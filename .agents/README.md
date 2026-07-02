# `.agents/`

This directory contains Codex project skills migrated from the previous Claude
Code setup.

## Skills

Project skills live in `.agents/skills/<skill-name>/SKILL.md`. Codex discovers
these skills and can use them when the user names the skill or asks for a
matching workflow.

Current skills:

- `check-ddd`
- `openspec-apply-change`
- `openspec-archive-change`
- `openspec-cleanup`
- `openspec-explore`
- `openspec-new-change`
- `openspec-propose`
- `openspec-sync-specs`
- `openspec-verify-change`
- `update-docs-map`

## Migration Notes

- These skills were migrated from `.claude/skills/`.
- Keep Codex-facing instructions here. Treat `.claude/skills/` as legacy unless
  the project explicitly decides to keep both assistants in sync.
- Avoid Claude-specific tool names such as `AskUserQuestion`, `TodoWrite`,
  `Read`, `Write`, `Glob`, or `Task`; describe actions in Codex terms instead.
