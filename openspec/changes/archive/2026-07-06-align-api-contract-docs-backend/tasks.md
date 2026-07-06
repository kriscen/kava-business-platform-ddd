## 1. 契约事实确认

- [x] 1.1 核对 `JsonResult<T>` 字段和错误响应构造。验收：后端 design/docs 明确 `success/data/errorCode/errorMessage`。
- [x] 1.2 核对 `PagingInfo<T>` 字段。验收：后端 design/docs 明确 `list/total/pageNo/pageSize`。
- [x] 1.3 核对 Long ID 序列化当前实际行为。验收：文档明确 ID 当前按 number 还是 string 传输，并与代码或配置依据一致。

## 2. UPMS 模块文档校正

- [x] 2.1 核对 UPMS HTTP Controller、API model、application service、repository 数量。验收：文档不再混用过期的 14 个资源描述。
- [x] 2.2 更新 `docs/06-modules/kbpd-upms/overview.md`。验收：App、TenantApp、16 个 Controller、RPC、权限和 deferred 项状态准确。
- [x] 2.3 更新 `docs/06-modules/kbpd-upms/business-rules.md`。验收：租户应用订阅、App 菜单绑定、MetaObjectHandler、DataScope deferred 状态描述一致。
- [x] 2.4 更新 `docs/05-frontend/upms-api.md` 如有过期字段或数量描述。验收：前端可按文档对接所有已实现 UPMS REST 端点。

## 3. Common / Member / 架构文档校正

- [x] 3.1 更新 Common 文档中的数据库基础设施状态。验收：`KavaMetaObjectHandler`、TenantLine、DataScope、Pagination 的实现/延期状态准确。
- [x] 3.2 更新 Member 文档的 MVP 边界。验收：已实现 DDD/持久化/RPC 桩与未实现应用服务/HTTP Controller 明确分离。
- [x] 3.3 更新 `docs/00-project-map.md`。验收：文档地图描述与修改后的模块文档一致。

## 4. 后端 OpenSpec 与验证

- [x] 4.1 更新后端相关 specs。验收：`api-patterns` 或新增 docs alignment spec 明确响应包装和分页契约。
- [x] 4.2 运行 `openspec validate align-api-contract-docs-backend`。验收：通过（2026-07-06）。
- [x] 4.3 运行必要的编译验证。验收：本次仅修改 docs/OpenSpec，未触及 Java 代码或配置，无需 Maven 编译。
