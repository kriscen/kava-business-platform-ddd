# `.Codex/rules/`

This directory stores project-specific rules that should stay separate from the
generated root `AGENTS.md`.

## Usage

- Put each rule in a standalone `.md` file, such as `project-docs.md`.
- Reference rules from `AGENTS.md` using Codex-readable import lines:

  ```markdown
  @.Codex/rules/project-docs.md
  ```

- Keep the nearby `KAVA-PROJECT-RULES` HTML comments in `AGENTS.md`; they make
  the project-specific imports easy to restore if the root agent file is
  regenerated.

## Why This Exists

`AGENTS.md` is the Codex entry point for repository instructions. Keeping
long-lived project rules in this directory avoids losing them when the root
instructions are refreshed, while still making them available to Codex through
explicit imports.

## Adding Rules

1. Add `.Codex/rules/<topic>.md`.
2. Add `@.Codex/rules/<topic>.md` to `AGENTS.md`.
3. Commit both files.
