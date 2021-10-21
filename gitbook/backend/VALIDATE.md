# 参数验证

## 控制器层
> Controller 继承BaseController
``` JAVA
public class PlfGoodController extends BaseController
```

> @RequestParam参数属性required为true
> 参数名不传时，非空验证才生效
``` JAVA
@GetMapping("testGetParamValid")
    public void testParamValid(@ApiParam(value = "用户名", required = true)
                               @RequestParam String name,
                               @ApiParam(value = "性别", required = true)
                               @RequestParam Integer sex)
```

> @RequestParam参数，自定义验证消息
> 参数存在且无值，验证自定义消息才有效
> 验证 ***枚举类型*** 时必须增加验证注解，才会生效，RequestParam.required = true解决了属性必须，验证注解可解决值一定是枚举里所定义的
``` JAVA
@GetMapping("testGetParamCustomMsgValid")
public void testGetParamCustomMsgValid(@ApiParam(value = "用户名", required = true)
                                         @NotBlank(message = "用户名不能为空")
                                         @RequestParam(required = true) String name,
                                         @ApiParam(value = "性别", required = true)
                                         @NotNull(message = "性别不能为空")
                                         @RequestParam(required = true) Integer sex,
                                         @ApiParam(value = "课程类型", required = true)
                                         @NotNull(message = "课程类型不正确")
                                         @RequestParam(required = true) CourseEnum courseEnum
                                         )
```

> POST/GET请求，自定义验证消息 **@Validated**
> **GET请求不要加@RequestBody**
 * **所有需要验证的属性，message不能为空**
 * @NotEmpty 用在集合上面
 * @NotBlank用在String上面
 * @NotNull用在基本数据类型上面

```JAVA
@PostMapping("testPOSTParamsValid")
public void testPOSTParamsValid(@Validated @RequestBody LoginDTO loginDTO)

@GetMapping("testPOSTParamsValid")
public void testGetParamsValid(@Validated LoginDTO loginDTO)  
  
/**
 * <p>
 * 登录入参
 * </p>
 *
 * @author miaoyj
 * @since 2020-07-09
 * @version 1.0.0-SNAPSHOT
 */
@Data
public class LoginDTO implements Serializable {
    @ApiModelProperty(value = "用户昵称", example = "大力", required = true)
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;
    @ApiModelProperty(value = "手机号", example = "11111111111", required = true)
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号格式不正确")
    private String telephone;
    @ApiModelProperty(value = "课程类型", example = "101", required = true)
    private CourseEnum courseEnum;
}
```

## 业务层逻辑验证 
> 例：Assert.isTrue(false, "合同号不能为空");[更多用法][hutool.Assert]
``` JAVA
    public void testAssert() {
        Assert.isTrue(false, "合同号不能为空");
        StaticLog.info("testAssert");
    }    
```


## 分组验证

> **必须继承BaseDTO**
>
> 使用场景：同一个DTO，新增id非必传，修改时id必传


``` JAVA
@Data
public class SampleTempDTO extends BaseDTO {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    private String cardNo;
} 

//新增
public ResultWrapper add(@Validated @RequestBody SampleBatchDTO dto){}

//修改
public ResultWrapper update(@Validated(BaseDTO.Update.class)@RequestBody SampleBatchDTO dto){}
```
