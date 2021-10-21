# 代码规范

## 阿里代码规范文档

https://miaoyj.oss-cn-shanghai.aliyuncs.com/alijava.pdf



## 项目结构

![image-20210922172701694](../assets/image-20210922172701694.png)

>com.boot.admin.${projectName}
>
>>constant--常量
>>${模块名}/api
>
>>>dto - 传输类（入参）
>
>>>vo - 输出类（出参）
>
>>>enums - 枚举类
>>
>>${模块名}/
>

>> > > domain - DO
>
>>>> function - 日志转换
>
>>>> mapper - mybatis mapper\xml
>
>>>> mapstruct - 对象转换
>
>>>> rest - 控制器
>
>>>> service - 业务实现
>

### 代码规范检查项
+ **POJO**
  
    1. DO：数据库操作表类
    
    2. DTO：入参
    
    3. VO：出参
    
    4. 【强制】所有的 POJO 类属性必须使用包装数据类型
    
    5.  如果参数值是固定的某些值，如：删除状态、激活状态等，要定义枚举类
    
       
    
+ **Controller**
  
	1. 基础url为复数，下划线分割，如：@ApiRestController("students")
	
	2. 单个请求的代码保持2行左右，具体实现由service处理
	
    3. swagger定义检查内容

       - 出入参是否合理，不要定义Map，Object等没有包含swagger注解说明的参数
       - 参数验证，枚举、非空，长度等
       -  tags关联版本号
       - 出入参全部定义在api模块
    
    4. 只允许调用service处理，不允许引入mapper，DO等 
    
    5. 定义枚举处理固定的某些值，如：动态排序，状态等
    
       
  
+ **Service** 
	
	1. 外部调用的方法，出/入参不会使用DO
	
	2. 单表CRUD，使用BaseService内提供的方法，如this.removeById(id)
	
	3. 调用mapper内的自定义方法，如this.baseMapper.pageVo(page)
	
	   


* **Service/mapper 层方法命名规约**
  
    1. 获取单个对象的方法用 get 做前缀
    
    2. 获取多个对象的方法用 list 做前缀，复数结尾，如:listObjects
    
    3. 获取统计值的方法用 count 做前缀
    
	4. 插入的方法用 save/insert 做前缀
	
	5. 删除的方法用 remove/delete 做前缀
	
	6. 修改的方法用 update 做前缀
	
	7. page分页
	
	   
	
+ **liquibase**
    1. 检查动作是否包含回滚语句
+ **提交前**
    1. 使用**IDEA阿里代码规范插件**扫描
    2. 执行：`$ cd boot-admin-web-demo && mvn clean javadoc:fix javadoc:jar`，自动填充代码注释@version，**代码注释不规范会使控制台输出的WARNING，这时修改对应的代码注释，再重新执行命令**
    3. push/pull，如遇到冲突优先使用rebase，而不是merge