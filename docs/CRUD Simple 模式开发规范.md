**适用场景**<font style="color:rgb(143, 145, 168);">：后台管理系统、配置类、低业务复杂度模块（如字典、区域、角色、菜单等）  
</font>**架构风格**<font style="color:rgb(143, 145, 168);">：六边形架构 + DDD 分层 + Simple Repository（非 CQRS）  
</font>**核心原则**<font style="color:rgb(143, 145, 168);">：职责清晰、类型安全、可测试、可演进 </font>

---

## <font style="color:rgb(44, 44, 54);">一、分层结构与职责</font>
| | | |
| --- | --- | --- |
| **<font style="color:rgb(17, 24, 39);">Adapter（适配器层）</font>** | <font style="color:rgb(29, 29, 32);">对接外部协议（HTTP/gRPC），参数校验，调用 Application Service，返回统一响应</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">com.xxx.adapter.http</font>` |
| **<font style="color:rgb(17, 24, 39);">Application（应用层）</font>** | <font style="color:rgb(29, 29, 32);">用例编排、事务边界、DTO 转换、调用 Repository</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">com.xxx.application.service</font>` |
| **<font style="color:rgb(17, 24, 39);">Domain（领域层）</font>** | <font style="color:rgb(29, 29, 32);">实体（Entity）、值对象（ValueObject）、聚合根（AggregateRoot）、领域服务（Domain Service）、Repository 接口</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">com.xxx.domain.model.*</font>`<font style="color:rgb(29, 29, 32);">   </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">com.xxx.domain.repository</font>`<font style="color:rgb(29, 29, 32);">   </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">com.xxx.domain.service</font>` |
| **<font style="color:rgb(17, 24, 39);">Infrastructure（基础设施层）</font>** | <font style="color:rgb(29, 29, 32);">Repository 实现、持久化对象（PO）、MyBatis Mapper、PO/Entity 转换器</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">com.xxx.infrastructure.*</font>` |


<font style="color:rgb(143, 145, 168);">✅</font><font style="color:rgb(143, 145, 168);"> </font>**调用方向**<font style="color:rgb(143, 145, 168);">：Adapter → Application → Domain（接口） ← Infrastructure（实现） </font>

---

## <font style="color:rgb(44, 44, 54);">二、对象命名与类型规范</font>
| | | | |
| --- | --- | --- | --- |
| <font style="color:rgb(29, 29, 32);">HTTP 请求参数</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Request</font>`<font style="color:rgb(29, 29, 32);">/</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Query</font>` | <font style="color:rgb(29, 29, 32);">Adapter</font> | <font style="color:rgb(29, 29, 32);">如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaRequest</font>`<font style="color:rgb(29, 29, 32);">,</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaQuery</font>` |
| <font style="color:rgb(29, 29, 32);">Application 命令</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Command</font>` | <font style="color:rgb(29, 29, 32);">Application</font> | <font style="color:rgb(29, 29, 32);">如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaCreateCommand</font>` |
| <font style="color:rgb(29, 29, 32);">Application 查询参数</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">*Query</font>`<font style="color:rgb(29, 29, 32);">（值对象）</font> | <font style="color:rgb(29, 29, 32);">Domain</font> | <font style="color:rgb(29, 29, 32);">如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaListQuery implements ValueObject</font>` |
| <font style="color:rgb(29, 29, 32);">Application 响应 DTO</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">DTO</font>` | <font style="color:rgb(29, 29, 32);">Application</font> | <font style="color:rgb(29, 29, 32);">如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaAppDetailDTO</font>`<font style="color:rgb(29, 29, 32);">,</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaListQueryDTO</font>` |
| <font style="color:rgb(29, 29, 32);">HTTP 响应对象</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Response</font>` | <font style="color:rgb(29, 29, 32);">Adapter</font> | <font style="color:rgb(29, 29, 32);">如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaResponse</font>`<font style="color:rgb(29, 29, 32);">,</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaListResponse</font>` |
| <font style="color:rgb(29, 29, 32);">主键/唯一标识</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Id</font>` | <font style="color:rgb(29, 29, 32);">Domain</font> | <font style="color:rgb(29, 29, 32);">实现</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Identifier</font>`<font style="color:rgb(29, 29, 32);">，如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaId</font>` |
| <font style="color:rgb(29, 29, 32);">领域实体</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Entity</font>` | <font style="color:rgb(29, 29, 32);">Domain</font> | <font style="color:rgb(29, 29, 32);">实现</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Entity<Id></font>`<font style="color:rgb(29, 29, 32);">，如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaEntity</font>` |
| <font style="color:rgb(29, 29, 32);">持久化对象</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">PO</font>` | <font style="color:rgb(29, 29, 32);">Infrastructure</font> | <font style="color:rgb(29, 29, 32);">继承</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysDeletablePO</font>`<font style="color:rgb(29, 29, 32);">，如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaPO</font>` |
| <font style="color:rgb(29, 29, 32);">Repository 接口</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">I*Repository</font>` | <font style="color:rgb(29, 29, 32);">Domain</font> | <font style="color:rgb(29, 29, 32);">继承</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">IBaseSimpleRepository</font>` |
| <font style="color:rgb(29, 29, 32);">Repository 实现</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">*Repository</font>` | <font style="color:rgb(29, 29, 32);">Infrastructure</font> | <font style="color:rgb(29, 29, 32);">如</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaRepository</font>` |


---

## <font style="color:rgb(44, 44, 54);">三、Repository 设计规范</font>
### <font style="color:rgb(44, 44, 54);">3.1 接口继承</font>
```properties
public interface ISysAreaRepository
extends IBaseSimpleRepository<SysAreaId, SysAreaEntity, SysAreaListQuery> {
// 可扩展自定义方法
List<SysAreaEntity> selectTreeList(SysAreaListQueryquery);
}
```



### <font style="color:rgb(44, 44, 54);">3.2 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">IBaseSimpleRepository</font>`<font style="color:rgb(44, 44, 54);"> 方法定义</font>
```properties
I create(Eentity);
Boolean update(Eentity);
Boolean removeBatchByIds(List<I> ids);
PagingInfo<E> queryPage(Qquery); // Q 为 ValueObject
E queryById(Iid);
```



<font style="color:rgb(143, 145, 168);">⚠️</font><font style="color:rgb(143, 145, 168);"> </font>**注意**<font style="color:rgb(143, 145, 168);">：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">queryPage</font>`<font style="color:rgb(143, 145, 168);"> 必须根据 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Q</font>`<font style="color:rgb(143, 145, 168);"> 中的字段构建动态查询条件（如 MyBatis-Plus LambdaQuery） </font>

---

## <font style="color:rgb(44, 44, 54);">四、Application Service 规范</font>
### <font style="color:rgb(44, 44, 54);">4.1 接口方法命名</font>
| | |
| --- | --- |
| <font style="color:rgb(29, 29, 32);">创建</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">createXxx(XxxCreateCommand command)</font>`<font style="color:rgb(29, 29, 32);">→ 返回</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Id</font>` |
| <font style="color:rgb(29, 29, 32);">更新</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">updateXxx(XxxUpdateCommand command)</font>`<font style="color:rgb(29, 29, 32);">→</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">void</font>` |
| <font style="color:rgb(29, 29, 32);">批量删除</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">removeXxxBatchByIds(List<Id> ids)</font>`<font style="color:rgb(29, 29, 32);">→</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">void</font>` |
| <font style="color:rgb(29, 29, 32);">分页查询</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">queryXxxPage(XxxListQuery query)</font>`<font style="color:rgb(29, 29, 32);">→</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">PagingInfo<DTO></font>` |
| <font style="color:rgb(29, 29, 32);">详情查询</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">queryXxxById(Id id)</font>`<font style="color:rgb(29, 29, 32);">→</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">DTO</font>` |
| <font style="color:rgb(29, 29, 32);">自定义查询</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">selectXxxTree(XxxListQuery query)</font>`<font style="color:rgb(29, 29, 32);">→</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">List<Tree<Long>></font>` |


### <font style="color:rgb(44, 44, 54);">4.2 事务要求</font>
+ <font style="color:rgb(44, 44, 54);">所有写操作（create/update/remove）</font>**<font style="color:rgb(17, 24, 39);">必须添加 </font>**`**<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">@Transactional</font>**`
+ <font style="color:rgb(44, 44, 54);">读操作无需事务</font>

### <font style="color:rgb(44, 44, 54);">4.3 分页处理</font>
+ <font style="color:rgb(44, 44, 54);">Repository 返回 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">PagingInfo<Entity></font>`
+ <font style="color:rgb(44, 44, 54);">Application 层转换为 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">PagingInfo<DTO></font>`

```properties
List<DTO> dtos = entities.stream().map(converter::toDTO).toList();
return PagingInfo.toResponse(dtos, originalPagingInfo);
```

---

## <font style="color:rgb(44, 44, 54);">五、对象转换规范（MapStruct）</font>
| | | |
| --- | --- | --- |
| **<font style="color:rgb(17, 24, 39);">Adapter Converter</font>** | <font style="color:rgb(29, 29, 32);">HTTP </font><font style="color:rgb(29, 29, 32);">↔</font><font style="color:rgb(29, 29, 32);"> Application</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaAdapterConverter</font>` |
| **<font style="color:rgb(17, 24, 39);">Application Converter</font>** | <font style="color:rgb(29, 29, 32);">Command/DTO </font><font style="color:rgb(29, 29, 32);">↔</font><font style="color:rgb(29, 29, 32);"> Entity</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaAppConverter</font>` |
| **<font style="color:rgb(17, 24, 39);">Infrastructure Converter</font>** | <font style="color:rgb(29, 29, 32);">Entity </font><font style="color:rgb(29, 29, 32);">↔</font><font style="color:rgb(29, 29, 32);"> PO</font> | `<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">SysAreaConverter</font>` |


<font style="color:rgb(143, 145, 168);">✅</font><font style="color:rgb(143, 145, 168);"> 所有转换器需标注 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">@Mapper(componentModel = "spring")</font>`

---

## <font style="color:rgb(44, 44, 54);">六、分页与查询参数传递</font>
### <font style="color:rgb(44, 44, 54);">6.1 参数流</font>
```properties
HTTP Query (SysAreaQuery)
 ↓ @ModelAttribute
Adapter → convertQueryDTO2QueryVal()
 ↓
Domain ValueObject (SysAreaListQuery { fields..., queryParam: QueryParamValObj })
 ↓
Repository.queryPage(query)
```

### <font style="color:rgb(44, 44, 54);">6.2 分页对象</font>
+ <font style="color:rgb(44, 44, 54);">前端传：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">pageNo</font>`<font style="color:rgb(44, 44, 54);">, </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">pageSize</font>`
+ <font style="color:rgb(44, 44, 54);">封装为：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">QueryParamValObj</font>`
+ <font style="color:rgb(44, 44, 54);">最终用于 MyBatis-Plus：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Page.of(pageNo, pageSize)</font>`

---

## <font style="color:rgb(44, 44, 54);">七、统一响应格式</font>
+ <font style="color:rgb(44, 44, 54);">使用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">JsonResult<T></font>`<font style="color:rgb(44, 44, 54);"> 封装返回</font>
+ <font style="color:rgb(44, 44, 54);">成功：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">JsonResult.buildSuccess(data)</font>`
+ <font style="color:rgb(44, 44, 54);">失败：</font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">JsonResult.buildError("msg")</font>`<font style="color:rgb(44, 44, 54);"> 或使用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">BaseErrorCodeEnum</font>`

```properties
@GetMapping("/page")
public JsonResult<PagingInfo<SysAreaListResponse>> getSysAreaPage(@ModelAttribute 
 SysAreaQuery query) {
  // ...
  return JsonResult.buildSuccess(result);
}
```



---

## <font style="color:rgb(44, 44, 54);">八、ID 类型安全实践</font>
+ <font style="color:rgb(44, 44, 54);">所有主键必须封装为 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Id</font>`<font style="color:rgb(44, 44, 54);"> 值对象</font>

```properties
public class SysAreaId implements Identifier {
  Longid;
}  
```

+ <font style="color:rgb(44, 44, 54);">避免在业务逻辑中直接使用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">Long id</font>`
+ <font style="color:rgb(44, 44, 54);">Controller 层需手动转换：</font>**<font style="color:rgb(44, 44, 54);">java</font>**<font style="color:rgb(108, 108, 108);background-color:rgb(226, 242, 255);">1</font><font style="color:rgb(44, 44, 54);background-color:rgba(204, 238, 255, 0.267);">SysAreaId.of(id) </font><font style="color:rgb(153, 68, 0);background-color:rgba(204, 238, 255, 0.267);">// 建议提供静态工厂方法</font>

<font style="color:rgb(143, 145, 168);">✅</font><font style="color:rgb(143, 145, 168);"> </font>**建议补充**<font style="color:rgb(143, 145, 168);">： </font>

```properties
public static SysAreaIdof(Longid) {
  return builder().id(id).build();
}
```

---

## <font style="color:rgb(44, 44, 54);">九、异常与校验</font>
+ **<font style="color:rgb(17, 24, 39);">参数校验</font>**<font style="color:rgb(44, 44, 54);">：使用 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">@Valid</font>`<font style="color:rgb(44, 44, 54);"> + JSR-380（如 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">@NotBlank</font>`<font style="color:rgb(44, 44, 54);">）</font>
+ **<font style="color:rgb(17, 24, 39);">全局异常处理</font>**<font style="color:rgb(44, 44, 54);">：需配置 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">@ControllerAdvice</font>`<font style="color:rgb(44, 44, 54);"> 捕获异常并返回 </font>`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">JsonResult</font>`
+ **<font style="color:rgb(17, 24, 39);">业务校验</font>**<font style="color:rgb(44, 44, 54);">：放在 Application Service 或 Domain Service 中</font>

---

<font style="color:rgb(143, 145, 168);">📌</font><font style="color:rgb(143, 145, 168);"> </font>**附：典型调用链路**

`<font style="color:rgb(97, 92, 237);background-color:rgb(239, 238, 255);">HTTP → Controller → AdapterConverter → AppService → AppConverter → Repository → InfraConverter → MyBatis → DB</font>`



