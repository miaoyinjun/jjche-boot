# 常见问题
1. Application.main与`$ mvn spring-boot:run`方式运行区别

   - Application.main运行，dev环境**推荐**
     1. 与boot-admin-parent集成开发时，修改boot-admin-parent代码后，不需要install
     
   - `$ mvn spring-boot:run`运行
     1. 与boot-admin-parent集成开发时，修改boot-admin-parent代码后，需要install

3. @AdminRestController与@ApiRestController区别?
   - 为了区分哪些是基础功能的url，
   - @AdminRestController会生成/api/admin的前缀的url，*基础功能接口*
   - @ApiRestController只会生成/api/的前缀的url，*新开发的接口*

4. BaseEntity与BaseEntityLogicDelete区别？
   - 不需要逻辑删除表的DO类继承BaseEntity
   - 需要逻辑删除表的DO类继承BaseEntityLogicDelete

5. 前缀/api/admin开头的，为什么在knife4j接口文档中看不到
   - 因/api/admin前缀下的为基础功能
   - 所有接口与前端对接完成，很少涉及到修改
   - 让开发者专注于新功能的开发，没有必要暴露出来
   - 配置boot.admin.swagger.ignore-filter-path可放开/api/admin

5. application.xml默认配置@@问题

   - application.yml统一获取maven的配置信息，**项目信息不需要在application.xml与pom.xml分别配置2遍**

   - ```yaml
     spring:
       application:
         name: "@project.name@"
     
     #获取maven属性在应用界面与细节/信息可获取到
     info:
       name: "@project.name@"
       description: "@project.description@"
       version: "@project.version@"
     ```

     

6. parent引入方式
   - 没有使用import方式，是因import会导致properties与plugins里的配置失效。
