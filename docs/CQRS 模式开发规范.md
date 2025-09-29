<font style="color:rgb(143, 145, 168);">适用于 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">com.kava.kbpd</font>`<font style="color:rgb(143, 145, 168);"> 项目中基于六边形架构（Hexagonal Architecture）与领域驱动设计（DDD）的 CQRS 实现规范。 </font>

---

## <font style="color:rgb(44, 44, 54);">1. 架构概览</font>
<font style="color:rgb(44, 44, 54);">本项目采用 </font>**<font style="color:rgb(17, 24, 39);">六边形架构 + DDD + CQRS</font>**<font style="color:rgb(44, 44, 54);"> 分层模型，整体结构如下：</font>



```properties
┌────────────────────┐
│ Adapter 层 │ ← HTTP / RPC / MQ 等外部适配器
└─────────┬──────────┘
 ↓
┌────────────────────┐
│ Application 层 │ ← 用例编排、事务边界、协调领域对象
└─────────┬──────────┘
 ↓
┌────────────────────┐
│ Domain 层 │ ← 聚合根、值对象、领域服务、Repository 接口（端口）
└─────────┬──────────┘
 ↓
┌────────────────────┐
│ Infrastructure 层 │ ← Repository 实现、Mapper、PO、转换器
└────────────────────┘
```

<font style="color:rgb(44, 44, 54);">CQRS 模式体现在 </font>**<font style="color:rgb(17, 24, 39);">读写分离的 Repository 接口设计</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">IBaseReadRepository</font>`<font style="color:rgb(44, 44, 54);">：仅用于查询（返回 Entity 或 DTO）</font>
+ `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">IBaseWriteRepository</font>`<font style="color:rgb(44, 44, 54);">：仅用于写操作（创建、更新、删除）</font>

---

## <font style="color:rgb(44, 44, 54);">2. 各层职责规范</font>
### <font style="color:rgb(44, 44, 54);">2.1 Adapter 层（适配器层）</font>
**<font style="color:rgb(17, 24, 39);">职责</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ <font style="color:rgb(44, 44, 54);">接收外部请求（HTTP、Dubbo、MQ 等）</font>
+ <font style="color:rgb(44, 44, 54);">参数校验（基础格式校验）</font>
+ <font style="color:rgb(44, 44, 54);">调用 Application Service</font>
+ <font style="color:rgb(44, 44, 54);">转换响应为外部协议格式（如 JSON）</font>

**<font style="color:rgb(17, 24, 39);">禁止行为</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ <font style="color:rgb(44, 44, 54);">❌</font><font style="color:rgb(44, 44, 54);"> 直接调用 Repository</font>
+ <font style="color:rgb(44, 44, 54);">❌</font><font style="color:rgb(44, 44, 54);"> 包含业务逻辑或领域规则</font>
+ <font style="color:rgb(44, 44, 54);">❌</font><font style="color:rgb(44, 44, 54);"> 操作 Entity 或 PO</font>

**<font style="color:rgb(17, 24, 39);">示例</font>**<font style="color:rgb(44, 44, 54);">：</font>

```properties
@RestController
public class SysUserController {
  @Resource
  private ISysUserAppServicesys UserAppService; // ✅ 仅依赖 Application Service
  @GetMapping("/{id}")
  public JsonResult<SysUserResponse> getDetails(@PathVariable Longid) {
    SysUserAppDetailDTOdto = sysUserAppService.queryUserById(SysUserId.of(id));
    return JsonResult.buildSuccess(converter.convert(dto));
  }
}
```



---

### <font style="color:rgb(44, 44, 54);">2.2 Application 层（应用层）</font>
**<font style="color:rgb(17, 24, 39);">职责</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ <font style="color:rgb(44, 44, 54);">定义用例接口（</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">ISysUserAppService</font>`<font style="color:rgb(44, 44, 54);">）</font>
+ <font style="color:rgb(44, 44, 54);">编排领域对象与 Repository 调用</font>
+ <font style="color:rgb(44, 44, 54);">管理事务边界（</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">@Transactional</font>`<font style="color:rgb(44, 44, 54);">）</font>
+ <font style="color:rgb(44, 44, 54);">调用领域服务（Domain Service）执行复杂业务规则</font>
+ <font style="color:rgb(44, 44, 54);">转换 Domain 对象 </font><font style="color:rgb(44, 44, 54);">↔</font><font style="color:rgb(44, 44, 54);"> DTO</font>

**<font style="color:rgb(17, 24, 39);">规范</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> 所有业务入口必须经过 Application Service</font>
+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> 读操作调用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">ReadRepository</font>`<font style="color:rgb(44, 44, 54);">，写操作调用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">WriteRepository</font>`
+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> 不包含具体业务规则（应委托给 Domain Service 或 Entity）</font>

**<font style="color:rgb(17, 24, 39);">示例</font>**<font style="color:rgb(44, 44, 54);">：</font>

```properties
@Service
public class SysUserAppService implements ISysUserAppService {
  @Resource private ISysUserReadRepository readRepository;
  @Resource private ISysUserWriteRepository writeRepository;
  @Resource private ISysUserService domainService; // 领域服务
  @Override
  @Transactional
  public SysUserIdcreateUser(SysUserCreateCommand command) {
    // 1. 转换 Command → Entity
    SysUserEntityentity = converter.convert(command);
    // 2. 领域校验（可选）
    domainService.validateCreate(entity);
    // 3. 写入
    return writeRepository.create(entity);
  }
}
```



---

### <font style="color:rgb(44, 44, 54);">2.3 Domain 层（领域层）</font>
**<font style="color:rgb(17, 24, 39);">核心组件</font>**<font style="color:rgb(44, 44, 54);">：</font>

| | |
| --- | --- |
| **<font style="color:rgb(17, 24, 39);">AggregateRoot</font>** | <font style="color:rgb(29, 29, 32);">聚合根（如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserEntity</font>`<font style="color:rgb(29, 29, 32);">），封装状态与行为</font> |
| **<font style="color:rgb(17, 24, 39);">ValueObject</font>** | <font style="color:rgb(29, 29, 32);">值对象（如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserId</font>`<font style="color:rgb(29, 29, 32);">,</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserListQuery</font>`<font style="color:rgb(29, 29, 32);">），不可变、无 ID</font> |
| **<font style="color:rgb(17, 24, 39);">Identifier</font>** | <font style="color:rgb(29, 29, 32);">实现</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Identifier</font>`<font style="color:rgb(29, 29, 32);">接口的 ID 类型</font> |
| **<font style="color:rgb(17, 24, 39);">Domain Service</font>** | <font style="color:rgb(29, 29, 32);">跨聚合或复杂逻辑（如唯一性校验）</font> |
| **<font style="color:rgb(17, 24, 39);">Repository 接口</font>** | <font style="color:rgb(29, 29, 32);">定义“端口”，如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">ISysUserReadRepository</font>` |


**<font style="color:rgb(17, 24, 39);">规范</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> Entity 应包含业务行为方法（如 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">lock()</font>`<font style="color:rgb(44, 44, 54);">, </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">changePassword()</font>`<font style="color:rgb(44, 44, 54);">）</font>
+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> 所有 ID 必须封装为值对象（如 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserId.of(1L)</font>`<font style="color:rgb(44, 44, 54);">）</font>
+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> 查询参数应封装为 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">ValueObject</font>`<font style="color:rgb(44, 44, 54);">（如 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserListQuery</font>`<font style="color:rgb(44, 44, 54);">）</font>

---

### <font style="color:rgb(44, 44, 54);">2.4 Infrastructure 层（基础设施层）</font>
**<font style="color:rgb(17, 24, 39);">职责</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ <font style="color:rgb(44, 44, 54);">实现 Repository 接口（</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserReadRepository</font>`<font style="color:rgb(44, 44, 54);"> / </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserWriteRepository</font>`<font style="color:rgb(44, 44, 54);">）</font>
+ <font style="color:rgb(44, 44, 54);">数据库操作（MyBatis Plus Mapper）</font>
+ <font style="color:rgb(44, 44, 54);">PO </font><font style="color:rgb(44, 44, 54);">↔</font><font style="color:rgb(44, 44, 54);"> Entity 转换（MapStruct）</font>

**<font style="color:rgb(17, 24, 39);">规范</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> Repository 实现类必须标注 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">@Repository</font>`
+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> 仅在此层操作 PO（Persistent Object）</font>
+ <font style="color:rgb(44, 44, 54);">✅</font><font style="color:rgb(44, 44, 54);"> 查询条件必须从 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserListQuery</font>`<font style="color:rgb(44, 44, 54);"> 中提取并构建 QueryWrapper</font>

**<font style="color:rgb(17, 24, 39);">示例</font>**<font style="color:rgb(44, 44, 54);">：</font>

```properties
@Repository
public class SysUserReadRepository implements ISysUserReadRepository {
  @Override
  public PagingInfo<SysUserEntity> queryPage(SysUserListQuery query) {
    // 从 query.getQueryParam() 获取分页参数
    // 未来可扩展：从 query 中提取 username、deptId 等条件
    Page<SysUserPO> page = sysUserMapper.selectPage(..., buildWrapper(query));
    return convertToPagingInfo(page);
  }
}
```



---

## <font style="color:rgb(44, 44, 54);">3. CQRS 接口定义规范</font>
### <font style="color:rgb(44, 44, 54);">3.1 读模型（Read Model）</font>
+ <font style="color:rgb(44, 44, 54);">接口继承 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">IBaseReadRepository<I, E></font>`
+ <font style="color:rgb(44, 44, 54);">方法命名以 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">query</font>`<font style="color:rgb(44, 44, 54);"> 开头</font>
+ <font style="color:rgb(44, 44, 54);">返回类型为 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Entity</font>`<font style="color:rgb(44, 44, 54);"> 或 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">PagingInfo<Entity></font>`
+ **<font style="color:rgb(17, 24, 39);">不包含写操作</font>**

```properties
public interface ISysUserReadRepository extends IBaseReadRepository<SysUserId, SysUserEntity> {
  PagingInfo<SysUserEntity> queryPage(SysUserListQueryquery);
}
```



### <font style="color:rgb(44, 44, 54);">3.2 写模型（Write Model）</font>
+ <font style="color:rgb(44, 44, 54);">接口继承 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">IBaseWriteRepository<I, E></font>`
+ <font style="color:rgb(44, 44, 54);">方法命名：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">create</font>`<font style="color:rgb(44, 44, 54);"> / </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">update</font>`<font style="color:rgb(44, 44, 54);"> / </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">removeBatchByIds</font>`
+ <font style="color:rgb(44, 44, 54);">参数为完整 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Entity</font>`
+ <font style="color:rgb(44, 44, 54);">返回主键或操作结果</font>

```properties
public interface ISysUserWriteRepository extends IBaseWriteRepository<SysUserId, SysUserEntity> {
  // 继承 create/update/removeBatchByIds
}
```



<font style="color:rgb(143, 145, 168);">💡</font><font style="color:rgb(143, 145, 168);"> 对于简单 CRUD 场景，可使用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">IBaseSimpleRepository</font>`<font style="color:rgb(143, 145, 168);">，但仍需经过 Application 层。 </font>

---

## <font style="color:rgb(44, 44, 54);">4. 对象转换规范</font>
| | | |
| --- | --- | --- |
| <font style="color:rgb(29, 29, 32);">Adapter </font><font style="color:rgb(29, 29, 32);">↔</font><font style="color:rgb(29, 29, 32);"> Application</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">adapter.converter</font>` | <font style="color:rgb(29, 29, 32);">MapStruct</font> |
| <font style="color:rgb(29, 29, 32);">Application </font><font style="color:rgb(29, 29, 32);">↔</font><font style="color:rgb(29, 29, 32);"> Domain</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">application.converter</font>` | <font style="color:rgb(29, 29, 32);">MapStruct</font> |
| <font style="color:rgb(29, 29, 32);">Domain </font><font style="color:rgb(29, 29, 32);">↔</font><font style="color:rgb(29, 29, 32);"> Infrastructure (PO)</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">infrastructure.converter</font>` | <font style="color:rgb(29, 29, 32);">MapStruct</font> |


**<font style="color:rgb(17, 24, 39);">命名约定</font>**<font style="color:rgb(44, 44, 54);">：</font>

+ <font style="color:rgb(44, 44, 54);">Converter 接口名：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">{EntityName}AdapterConverter</font>`<font style="color:rgb(44, 44, 54);"> / </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">AppConverter</font>`<font style="color:rgb(44, 44, 54);"> / </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Converter</font>`
+ <font style="color:rgb(44, 44, 54);">方法名：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">convertX2Y</font>`

---

## <font style="color:rgb(44, 44, 54);">5. 查询参数传递链路</font>
```properties
SysUserQuery (Adapter)
 ↓ [AdapterConverter]
SysUserListQueryDTO (Application)
 ↓ [AppConverter]
SysUserListQuery (Domain, ValueObject)
 ↓
Repository.queryPage(query)
```



<font style="color:rgb(143, 145, 168);">⚠️</font><font style="color:rgb(143, 145, 168);"> 当前 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserListQuery</font>`<font style="color:rgb(143, 145, 168);"> 仅包含分页参数，后续应扩展业务查询字段（如 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">username</font>`<font style="color:rgb(143, 145, 168);">, </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">deptId</font>`<font style="color:rgb(143, 145, 168);">）。 </font>

---

## <font style="color:rgb(44, 44, 54);">6. 事务管理</font>
+ **<font style="color:rgb(17, 24, 39);">事务边界必须在 Application Service 方法上</font>**
+ <font style="color:rgb(44, 44, 54);">使用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">@Transactional</font>`<font style="color:rgb(44, 44, 54);"> 注解</font>
+ <font style="color:rgb(44, 44, 54);">读方法不应加事务（除非需一致性读）</font>

```properties
@Override
@Transactional
publicvoidupdateUser(SysUserUpdateCommandcommand) {
  // ...
}
```



---

## <font style="color:rgb(44, 44, 54);">7. 异常与错误处理</font>
+ <font style="color:rgb(44, 44, 54);">定义业务异常（如 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">UserNotFoundException</font>`<font style="color:rgb(44, 44, 54);">）</font>
+ <font style="color:rgb(44, 44, 54);">全局异常处理器统一返回 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">JsonResult</font>`
+ <font style="color:rgb(44, 44, 54);">错误码使用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">BaseErrorCodeEnum</font>`

---

## <font style="color:rgb(44, 44, 54);">8. 禁止事项清单</font>
| | |
| --- | --- |
| <font style="color:rgb(29, 29, 32);">Adapter 直接调用 Repository</font> | <font style="color:rgb(29, 29, 32);">破坏分层</font> |
| <font style="color:rgb(29, 29, 32);">Application 层包含 if-else 业务规则</font> | <font style="color:rgb(29, 29, 32);">应下沉到 Domain</font> |
| <font style="color:rgb(29, 29, 32);">使用原始类型（Long）表示 ID</font> | <font style="color:rgb(29, 29, 32);">必须用</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysUserId</font>` |
| <font style="color:rgb(29, 29, 32);">Entity 包含数据库注解（如 @Table）</font> | <font style="color:rgb(29, 29, 32);">属于 PO 职责</font> |
| <font style="color:rgb(29, 29, 32);">Repository 实现类放在 domain 包</font> | <font style="color:rgb(29, 29, 32);">必须在 infrastructure</font> |


---

## <font style="color:rgb(44, 44, 54);">9. 附录：典型调用链路</font>
### <font style="color:rgb(44, 44, 54);">新增用户（CQRS 写）</font>
```properties
HTTP POST /user
 → SysUserController.save()
 → ISysUserAppService.createUser(command)
 → SysUserAppConverter → SysUserEntity
 → ISysUserWriteRepository.create(entity)
 → SysUserWriteRepository (Infra)
 → SysUserMapper.insert()
```



### <font style="color:rgb(44, 44, 54);">分页查询（CQRS 读）</font>
```properties
HTTP GET /user/page
 → SysUserController.getSysAreaPage(query)
 → ISysUserAppService.queryUserPage(SysUserListQuery)
 → ISysUserReadRepository.queryPage(query)
 → SysUserReadRepository (Infra)
 → SysUserMapper.selectPage()
 → 转换 Entity → DTO → Response
```

---

<font style="color:rgb(143, 145, 168);">本规范适用于所有基于 CQRS 的业务模块。简单 CRUD 模块可使用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">IBaseSimpleRepository</font>`<font style="color:rgb(143, 145, 168);">，但</font>**仍需遵守 Adapter → Application → Repository 调用链**<font style="color:rgb(143, 145, 168);">。 </font>

