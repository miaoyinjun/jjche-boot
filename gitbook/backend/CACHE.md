# 缓存操作

> 所生成的key都会增加前缀
> 格式：${spring.application.name}:{keyName}，举例：boot-admin-demo:student

> 注解方式：JetCache，手动方式：RedisService


## RedisService

> 引入使用，具体操作方法进入RedisService自行查看

``` JAVA
    @Autowired
    RedisService redisService;
```

> 关系型格式规范

1: 表名,如student
2: 主键列名,如id
3: 主键值,如2,3,4...., a , b ,c
4: 第4段,写要存储的列名,如name
例：student:id:9:name

``` JAVA
String tableKey = RedisKeyUtil.getKey("student", "id", String.valueOf(value3));
redisService.stringSetString(tableKey, value1);
tableKey = RedisKeyUtil.getKeyWithColumn("student", "id", value2, "name");
redisService.stringSetString(tableKey, value1);
```

## JetCache
涉及自定义的key，请定义在常量或枚举里

- https://github.com/alibaba/jetcache/wiki/FAQ_CN
- https://github.com/alibaba/jetcache/wiki/MethodCache_CN
- https://github.com/alibaba/jetcache/wiki/CreateCache_CN
- https://github.com/alibaba/jetcache/pull/474

jetcache 与jpa的事务冲突问题 jpa Found shared references to a collection

方法1：删除事务注解

方法2：缓存执行放到最后