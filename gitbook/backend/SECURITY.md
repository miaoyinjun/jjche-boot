## 权限验证
1. 注解验证 @PreAuthorize("@el.check('student:list')")

   ```java
   		@GetMapping
       @ApiOperation(value = "学生-列表", tags = ApiVersion.VERSION_1_0_0)
       @PreAuthorize("@el.check('student:list')")
       @LogRecordAnnotation(
               value = "列表", category = LogCategoryType.MANAGER,
               type = LogType.SELECT, module = ApiVersion.MODULE_STUDENT
       )
       public ResultWrapper<MyPage<StudentVO>> pageQuery()
   ```

2. 注解忽略

   > @AnonymousDeleteMapping、@AnonymousGetMapping、@AnonymousPostMapping、@AnonymousPutMapping

3. 原理说明

   - 前端

     > Permission/permission.js取到标签里的权限标识或角色与info接口里的roles做比较(store/getter.js存储permission.js里需要的内容)

   - 后端

     > token.auth包含权限标识，在TokenFilter里从token取出auth设置到了Spring Security上下文中
     > Authentication authentication = tokenProvider.getAuthentication(token);
     > SecurityContextHolder.getContext().setAuthentication(authentication);


### 用户认证
1. 密码登录
2. 短信登录
3. 其它方式扩展可参考以上2种方式的代码

### 获取登录信息
- SecurityUtils.java

### 配置
> 前缀 boot.admin.security.
> 建议各环境不要使用相同的jwt.secret

|  参数                                | 说明                            | 默认值                   | 举例            |
|  :---:                              | :---:                           | :---:                   | :---:           |
| exclude-urls                        | 忽略拦截的地址，数组            | 无                      | - /demo/students/** |
| role-urls                           | 角色与url对应关系，数组            | 无                      | - role-name: ROLE_USER urls: - /demo/students/listPage |
| jwt.secret                          | token密钥                        | secret                  | abc            |
| jwt.access-token-expiration         | token过期时间(毫秒)               | 7200000/2小时            | 7200000        |
| jwt.refresh-token-expiration        | token刷新过期时间(毫秒)            | 7200000/2小时            | 7200000        |
| jwt.token-header                    | 授权所在的header参数名称           | Authorization            | auth           |