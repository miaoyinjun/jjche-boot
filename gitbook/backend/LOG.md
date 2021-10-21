## 日志
> 默认输出路径： ./logs

> 输出
> StaticLog提供了trace、debug、info、warn、error方法，提供变量占位符支持
` StaticLog.info("This is static {} log.", "test"); `

### 注解-通用操作日志组件
**@LogRecordAnnotation**

优化自 https://github.com/mouzt/mzt-biz-log/
此组件解决的问题是： 「谁」在「什么时间」对「什么」做了「什么事」

#### Change Log

| 优化内容 |
|----|
|整体日志拦截在方法执行之前执行|
|支持对象的diff|
|字段增加分类、类型、模块、异常记录|

## 使用方式
#### SpringBoot入口打开开关,添加 @EnableLogRecord 注解
tenant是代表租户的标识，一般一个服务或者一个业务下的多个服务都写死一个 tenant 就可以
```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableLogRecord(tenant = "com.mzt.test")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```
#### 日志埋点
###### 1. 普通的记录日志
* category：分类
* type：类型
* module：模块  
* value：操作内容，如：创建了一个用户, 用户姓名：「\{\{#dto.name\}\}」
* pefix：是拼接在 bizNo 上作为 log 的一个标识。避免 bizNo 都为整数 ID 的时候和其他的业务中的 ID 重复。比如订单 ID、用户 ID 等
* bizNo：就是业务的 ID，比如订单ID，我们查询的时候可以根据 bizNo 查询和它相关的操作日志
* detail：详细内容，如：修改内容：「{STUDENT_UPDATE_DIFF_BY_DTO{#dto\}\}」
* operatorId：可指定，如登录时，operatorId = "\{\{#authUser.username\}\}"  
* SpEL 表达式：其中用双大括号包围起来的（例如：\{\{#order.purchaseName\}\}）#order.purchaseName 是 SpEL表达式。Spring中支持的它都支持的。比如调用静态方法，三目表达式。SpEL 可以使用方法中的任何参数
```
  @LogRecordAnnotation(success = "\{\{#order.purchaseName\}\}下了一个订单,购买商品「\{\{#order.productName\}\}」,下单结果:\{\{#_ret\}\}",
              prefix = LogRecordType.ORDER, bizNo = "\{\{#order.orderNo\}\}")
  public boolean createOrder(Order order) {
      log.info("【创建订单】orderNo={}", order.getOrderNo());
      // db insert order
      return true;
  }
```
###### 2. 日志文案调整
**参考StudentNameByIdsParseFunction，StudentUpdateDiffByDtoParseFunction**

对于更新等方法，方法的参数上大部分都是订单ID、或者产品ID等，
比如下面的例子：日志记录的success内容是："更新了订单\{\{#orderId\}\},更新内容为...."，这种对于运营或者产品来说难以理解，所以引入了自定义函数的功能。
使用方法是在原来的变量的两个大括号之间加一个函数名称 例如 "{ORDER{#orderId\}\}" 其中 ORDER 是一个函数名称。只有一个函数名称是不够的,需要添加这个函数的定义和实现。可以看下面例子
自定义的函数需要实现框架里面的IParseFunction的接口，需要实现两个方法：

* functionName() 方法就返回注解上面的函数名；

* apply()函数参数是 "{ORDER{#orderId\}\}"中SpEL解析的#orderId的值，这里是一个数字1223110，接下来只需要在实现的类中把 ID 转换为可读懂的字符串就可以了，
  一般为了方便排查问题需要把名称和ID都展示出来，例如："订单名称（ID）"的形式。

> 这里有个问题：加了自定义函数后，框架怎么能调用到呢？
答：对于Spring boot应用很简单，只需要把它暴露在Spring的上下文中就可以了，可以加上Spring的 @Component 或者 @Service 很方便😄。Spring mvc 应用需要自己装配 Bean。

```
    // 没有使用自定义函数
    @LogRecordAnnotation(success = "更新了订单\{\{#orderId\}\},更新内容为....",
            prefix = LogRecordType.ORDER, bizNo = "\{\{#order.orderNo\}\}",
            detail = "\{\{#order.toString()\}\}")
    public boolean update(Long orderId, Order order) {
        return false;
    }

    //使用了自定义函数，主要是在 \{\{#orderId\}\} 的大括号中间加了 functionName
    @LogRecordAnnotation(success = "更新了订单{ORDER{#orderId\}\},更新内容为...",
            prefix = LogRecordType.ORDER, bizNo = "\{\{#order.orderNo\}\}",
            detail = "\{\{#order.toString()\}\}")
    public boolean update(Long orderId, Order order) {
        return false;
    }

    // 还需要加上函数的实现
    @Component
    public class OrderParseFunction implements IParseFunction {
        @Resource
        @Lazy //为了避免类加载顺序的问题 最好为Lazy，没有问题也可以不加
        private OrderQueryService orderQueryService;
        
        @Override 
        public String functionName() {
            //  函数名称为 ORDER
            return "ORDER";
        }
    
        @Override
        //这里的 value 可以吧 Order 的JSON对象的传递过来，然后反解析拼接一个定制的操作日志内容
        public String apply(String value) {
            if(StringUtils.isEmpty(value)){
                return value;
            }
            Order order = orderQueryService.queryOrder(Long.parseLong(value));
            //把订单产品名称加上便于理解，加上 ID 便于查问题
            return order.getProductName().concat("(").concat(value).concat(")");
        }
    }
```
###### 3.日志文案调整 使用 SpEL 三目表达式
```
    @LogRecordAnnotation(prefix = LogRecordTypeConstant.CUSTOM_ATTRIBUTE, bizNo = "\{\{#businessLineId\}\}",
            success = "\{\{#disable ? '停用' : '启用'\}\}了自定义属性{ATTRIBUTE{#attributeId\}\}")
    public CustomAttributeVO disableAttribute(Long businessLineId, Long attributeId, boolean disable) {
    	return xxx;
    }
```
###### 4. 日志文案调整 模版中使用方法参数之外的变量&函数中也可以使用Context中变量
可以在方法中通过 LogRecordContext.putVariable(variableName, Object) 的方法添加变量，第一个对象为变量名称，后面为变量的对象，
然后我们就可以使用 SpEL 使用这个变量了，例如：例子中的 \{\{#innerOrder.productName\}\} 是在方法中设置的变量，除此之外，在上面提到的自定义函数中也可以使用LogRecordContext中的变量。
（注意：LogRecordContext中变量的生命周期为这个方法，超出这个方法，方法中set到Context的变量就获取不到了）
```
    @Override
    @LogRecordAnnotation(
            success = "\{\{#order.purchaseName\}\}下了一个订单,购买商品「\{\{#order.productName\}\}」,测试变量「\{\{#innerOrder.productName\}\}」,下单结果:\{\{#_ret\}\}",
            prefix = LogRecordType.ORDER, bizNo = "\{\{#order.orderNo\}\}")
    public boolean createOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);
        return true;
    }
```

###### 5. 函数中使用LogRecordContext的变量

使用 LogRecordContext.putVariable(variableName, Object) 添加的变量除了可以在注解的 SpEL 表达式上使用，还可以在自定义函数中使用 这种方式比较复杂，下面例子中示意了列表的变化，比如
从[A,B,C] 改到 [B,D] 那么日志显示：「删除了A，增加了D」

```
    @LogRecord(success = "{DIFF_LIST{'文档地址'\}\}", bizNo = "\{\{#id\}\}", prefix = REQUIREMENT)
    public void updateRequirementDocLink(String currentMisId, Long id, List<String> docLinks) {
        RequirementDO requirementDO = getRequirementDOById(id);
        LogRecordContext.putVariable("oldList", requirementDO.getDocLinks());
        LogRecordContext.putVariable("newList", docLinks);

        requirementModule.updateById("docLinks", RequirementUpdateDO.builder()
                .id(id)
                .docLinks(docLinks)
                .updater(currentMisId)
                .updateTime(new Date())
                .build());
    }
    
    
    @Component
    public class DiffListParseFunction implements IParseFunction {
    
        @Override
        public String functionName() {
            return "DIFF_LIST";
        }
    
        @SuppressWarnings("unchecked")
        @Override
        public String apply(String value) {
            if (StringUtils.isBlank(value)) {
                return value;
            }
            List<String> oldList = (List<String>) LogRecordContext.getVariable("oldList");
            List<String> newList = (List<String>) LogRecordContext.getVariable("newList");
            oldList = oldList == null ? Lists.newArrayList() : oldList;
            newList = newList == null ? Lists.newArrayList() : newList;
            Set<String> deletedSets = Sets.difference(Sets.newHashSet(oldList), Sets.newHashSet(newList));
            Set<String> addSets = Sets.difference(Sets.newHashSet(newList), Sets.newHashSet(oldList));
            StringBuilder stringBuilder = new StringBuilder();
            if (CollectionUtils.isNotEmpty(addSets)) {
                stringBuilder.append("新增了 <b>").append(value).append("</b>：");
                for (String item : addSets) {
                    stringBuilder.append(item).append("，");
                }
            }
            if (CollectionUtils.isNotEmpty(deletedSets)) {
                stringBuilder.append("删除了 <b>").append(value).append("</b>：");
                for (String item : deletedSets) {
                    stringBuilder.append(item).append("，");
                }
            }
            return StringUtils.isBlank(stringBuilder) ? null : stringBuilder.substring(0, stringBuilder.length() - 1);
        }
    }
```