# mybatis

## sql输出
> 默认不输出，可配置环境变量：BOOT_ADMIN_DB_IS-PRINT-SQL

## service

1. 单表CRUD，使用BaseService内提供的方法，如this.removeById(id)

2. 调用mapper内的自定义方法，如this.baseMapper.pageVo(page)

   

## 分页

1. 控制器page分页参数定义

   ```java
     public ResultWrapper<MyPage<StudentVO>> pageQuery(PageParam page,
                                                      @ApiParam(value = "排序", required = true)
                                                      @NotNull(message = "排序字段不正确")
                                                      @RequestParam StudentSortEnum sort,
                                                      @ApiParam(value = "课程")
                                                      @RequestParam(required = false) CourseEnum course,
                                                      @Validated StudentQueryCriteriaDTO query) {
           return ResultWrapper.ok(studentService.pageQuery(page, sort, course, query));
       }
   ```

2. service处理分页

   ```java
    public MyPage<StudentVO> pageQuery(PageParam page, StudentSortEnum sort, CourseEnum course, StudentQueryCriteriaDTO query) {
           QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
           if (course != null) {
               queryWrapper.eq("course", course);
           }
   		   //this.page(page, queryWrapper);
           return this.baseMapper.pageQuery(page, sort, queryWrapper);
       }
   ```

3. mapper分页

   ```java
   MyPage<StudentVO> pageQuery(PageParam page, StudentSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);
   ```

   

## 注解@QueryCriteria查询说明

1. 定义DTO入参
```JAVA
@Data
public class tudentQueryCriteriaDTO{

    /**
    * 精确
    */
    @ApiModelProperty(value = "姓名")
    @QueryCriteria(propName = "name", type = QueryCriteria.Type.EQUAL)
    private String name;
}
```
2. 引入到控制器内，**不要使用注解@RequestParam**
```JAVA
    public ResultWrapper<MyPage<StudentVO>> pageQuery(PageParam page,
                                                   @ApiParam(value = "排序", required = true)
                                                   @NotNull(message = "排序字段不正确")
                                                   @RequestParam StudentSortEnum sort,
                                                   @ApiParam(value = "课程")
                                                   @RequestParam(required = false) CourseEnum course,
                                                   @Validated StudentQueryCriteriaDTO query) {
        return ResultWrapper.ok(studentService.pageQuery(page, sort, course, query));
    }
```
3. 转换得到查询条件构造器queryWrapper
- 自定义sql方式
```java
    public MyPage<StudentVO> pageQuery(PageParam page, StudentSortEnum sort, CourseEnum course, StudentQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        if (course != null) {
            queryWrapper.eq("course", course);
        }
        return this.baseMapper.pageQuery(page, sort, queryWrapper);
    }
```
- mybatis plus CRUD接口方式
```java
    public List<JobDO> queryAll(JobQueryCriteriaDTO criteria) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(criteria);
        return this.list(queryWrapper);
    }
```

## 逻辑删除
> 如果字段使用唯一索引，被逻辑删除后，如果再次插入会失败，这里的唯一索引要和逻辑删除，再加其它唯一字段（防止再次被删除）做联合索引可解决
> 修改人字段不会修改，解决方法


```java
/** 根据id逻辑删除*/
StudentDO studentDO = new StudentDO();
studentDO.setId(1L);
this.baseMapper.deleteByIdWithFill(studentDO);
#this.removeByIdWithFill(studentDO);

/**根据ids逻辑删除*/
studentDO = new StudentDO();
Wrapper wrapper = Wrappers.<StudentDO>lambdaQuery().in(StudentDO::getId, 1, 3);
this.removeBatchWithFill(studentDO, wrapper);

/**根据自定义条件，逻辑批量删除*/
studentDO = new StudentDO();
Set<Long> ids = CollUtil.newHashSet(3L, 4L);
this.removeBatchByIdsWithFill(studentDO, ids);

```
> 参考
>
> https://blog.csdn.net/yuanlintufang/article/details/106180260
> https://blog.csdn.net/qq_39313596/article/details/101039964
> https://mybatis.plus/guide/faq.html#%E9%80%BB%E8%BE%91%E5%88%A0%E9%99%A4%E4%B8%8B-%E8%87%AA%E5%8A%A8%E5%A1%AB%E5%85%85-%E5%8A%9F%E8%83%BD%E6%B2%A1%E6%9C%89%E6%95%88%E6%9E%9C


## 枚举支持
> 表示状态的字段，如锁定，正常，删除等，建议定义枚举表示，参见【枚举定义规范】


``` JAVA
IPage<StudentVO> pageVo(Page<?> page, @Param("name") String name, @Param("course") CourseEnum courseEnum);
```


```xml
<select id="pageVo" resultType="com.boot.api.demo.vo.StudentVO">
        SELECT * FROM students
        <if test="name != null">
            WHERE name LIKE concat('%',#{name},'%')
        </if>
        <if test="course != null">
            WHERE course = #{course}
        </if>
    </select>
```



### 支持动态排序枚举

1. 定义排序枚举
```java
/**
 * <p>
 * 学生 排序枚举
 * </p>
 *
 * @author miaoyj
 * @since 2021-02-02
 * @version 1.0.0-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum StudentSortEnum{

    /**
    * 主键
    */
    ID_DESC("id DESC", "id倒序"),
    ID_ASC("id ASC", "id正序"),
    ;

    /**
    * Constant <code>MAPPINGS</code>
    */
    private static final Map<String, StudentSortEnum> MAPPINGS;

    static {
        Map<String, StudentSortEnum> temp = new HashMap<String, StudentSortEnum>();
        for (StudentSortEnum courseEnum : values()) {
        temp.put(courseEnum.value, courseEnum);
        }
        MAPPINGS = Collections.unmodifiableMap(temp);
    }

    @JsonValue
    private final String value;
    private final String desc;

    /**
     * <p>
     * 根据index获取枚举
     * </p>
     *
     * @param index a String
     * @return 枚举
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StudentSortEnum resolve(String index) {
    return MAPPINGS.get(index);
    }
}
```
2. 枚举作为参数传入到mapper，即可自动插入排序语句
```java
List<StudentDO> queryAll(StudentSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);
```



## MyBatis读取大量数据（流式读取）

> 导出大量数据时，虚拟机频繁GC，内存耗尽，CPU爆满，可采用Mybatis数据流式读取进行优化。

> 场景：java端从数据库读取100W数据进行后台业务处理。
1. 分页读取出来。缺点：需要排序后分页读取，性能低下。
2. 一次性读取出来。缺点：需要很大内存，一般计算机不行。
3. 建立长连接，利用服务端游标，一条一条流式返回给java端。
4. jdbc中有个重要的参数fetchSize（它对业务实现无影响，即不会限制读取条数等），优化后可显著提升性能。

> JDBC三种读取方式：
1. 一次全部（默认）：一次获取全部。
2. 流式：多次获取，一次一行。
3. 游标：多次获取，一次多行。

1. Mapper层
``` JAVA
    <select id="exportAll" resultType="com.boot.api.demo.vo.StudentVO" resultSetType="FORWARD_ONLY"
            fetchSize="-2147483648">
        SELECT * FROM students
    </select>
```
2. Dao层
```java
void exportAll(ResultHandler<StudentVO> handler);
```
3. Service层
```java
this.baseMapper.exportAll(resultContext -> {
            StudentVO studentVO = resultContext.getResultObject();
            list.add(studentVO);
        });
```

> 原理分析
1. 先在服务端执行查询后将数据缓存在服务端。（耗时相对较长）
2. java端获取数据时，利用服务端游标进行指针跳动，如果fetchSize为1000，则一次性跳动1000条，返回给java端缓存起来。（耗时较短，跳动次数为N/1000）
3. 在调用next函数时，优先从缓存中取数，其次执行2过程。（内存读取，耗时可忽略）


~~https://mybatis.plus/guide/quick-start.html#%E7%BC%96%E7%A0%81~~
~~> 拦截sql里的et问题，BaseMapper~~



## ~~代码生成器~~

~~> 配置文件：mybatis-plus-maven-plugin.properties~~

~~> 生成命令: ~~
~~` $ mvn properties:read-project-properties boot-mybatis-plus:generate -X `~~ 

~~>将Controller里@RequestMapping的value字段修改为swagger扫描的字段，如：DemoApiConstant.API_URL + "/students"~~

~~> 需要*逻辑删除*字段的，在生成entity后修改继承为BaseEntityLogicDelete~~