## Project Documentation

The project maintains two distinct documentation locations, each serving a different purpose:

### `docs/` — Engineering Documentation (long-lived)

Stable project knowledge: architecture, conventions, API specs, module references, and decision history. Treat this as the canonical source of truth for "how the system works today".

- **Entry point**: [`docs/00-project-map.md`](../../docs/00-project-map.md) — navigation index of the entire `docs/` directory; read this first to locate any document.
- Structure: `01-sql/` (database scripts), `02-architecture/` (overview & boundaries), `03-conventions/` (code style, git), `04-reference/` (API spec, error codes), `05-history/` (ADRs), `06-modules/` (per-module docs).
- Whenever files under `docs/` are added/removed/renamed, run `/update-docs-map` to sync `00-project-map.md`.

### `openspec/` — Change Artifacts (short-lived, per-change)

OpenSpec change proposals and specs for in-flight work. Each change lives in its own subdirectory under `openspec/changes/`, containing proposal, design, specs, and tasks. Completed changes get archived; their delta specs are merged back into `openspec/specs/` (the main spec set).

- Use `openspec/` to drive new features/refactors via the OpenSpec workflow.
- Use `docs/` to look up established knowledge and document outcomes after a change ships.
