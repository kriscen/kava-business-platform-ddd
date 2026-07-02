## Project Documentation

The project maintains two distinct documentation locations, each serving a different purpose:

### `docs/` — Engineering Documentation (long-lived)

Stable project knowledge: architecture, conventions, API specs, module references, and decision history. Treat this as the canonical source of truth for "how the system works today".

- **Entry point**: [`docs/00-project-map.md`](../../docs/00-project-map.md) — navigation index of the entire `docs/` directory; read this first to locate any document.
- Structure: `01-sql/` (database scripts), `02-architecture/` (overview & boundaries), `03-conventions/` (code style, git), `04-reference/` (API spec, error codes), `05-history/` (ADRs), `06-modules/` (per-module docs).
- Whenever files under `docs/` are added/removed/renamed, use the `update-docs-map` skill to sync `00-project-map.md`.

### `openspec/` — Change Artifacts (short-lived, per-change)

OpenSpec change proposals and specs for in-flight work. Each change lives in its own subdirectory under `openspec/changes/`, containing proposal, design, specs, and tasks. Completed changes get archived; their delta specs are merged back into `openspec/specs/` (the main spec set).

- Use `openspec/` to drive new features/refactors via the OpenSpec workflow.
- Use `docs/` to look up established knowledge and document outcomes after a change ships.

### Archive -> Docs Sync Rules

After running the `openspec-archive-change` skill to archive a change, follow this process:

1. **Review archived content**: read the archived change's proposal, specs, design, and other artifacts. Extract long-lived engineering knowledge such as new APIs, changed business rules, and architecture decisions.
2. **Compare with current docs**: check whether related files already exist under `docs/`, and decide whether the archived content should be reflected there.
3. **Confirm with the user**: list the suggested docs updates and target files, then wait for user confirmation before editing docs. Do not update docs automatically.
4. **Refresh the docs index**: if docs were updated, use the `update-docs-map` skill to refresh `docs/00-project-map.md`.

**Do not sync** pure implementation details, temporary workarounds, or content that has already been superseded by a later change.
