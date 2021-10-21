# 接口文档
> 了解更多参考：https://doc.xiaominfo.com/
## 传输类

- **DTO为输入与传输类**，`@ApiModelProperty`定义名称、示例、是否必填

  ```java
     @Data
      public class LoginDTO implements Serializable {
  	  	@ApiModelProperty(value = "用户昵称", example = "大力", required = true)
    	  private String nickname;
      	@ApiModelProperty(value = "手机号", example = "11111111111", required = true)
  	    private String telephone;
    	  @ApiModelProperty(value = "课程类型", example = "101", required = true)
      	private CourseEnum courseEnum;
      }
  ```

- **VO(输出类)**，`@ApiModelProperty`定义名称

  ```java
   @Data
      @NoArgsConstructor
      public class LoginVO implements Serializable {
      	@ApiModelProperty(value = "用户昵称")
        private String name;
        @ApiModelProperty(value = "手机号", example = "11111111111")
        private String telephone;
        @ApiModelProperty(value = "课程类型", example = "101")
        private CourseEnum courseEnum;
      }    
  ```

## 控制器

> 定义`@Api`定义名称,`@ApiSupport`定义顺序与作者
``` JAVA
    @Api(tags = "平台商品")
    @ApiSupport(order = 1, author = "miaoyj")
    @RestController
    @RequestMapping(value = DemoApiConstant.URL + "/good/plf-good", produces = MediaType.APPLICATION_JSON_VALUE)
    public class DemoController {}
```

> 注意@RequestParam**属性required默认为true**，`@ApiOperation`定义方法名称, `@ApiParam`定义参数说明，**属性required默认为false**
``` JAVA
    @GetMapping
    @ApiOperation(value = "列表")
    public List<PlfGoodVO> list(@ApiParam(value = "商品名称", required = false)
                                @RequestParam(required = false) String plfName) {
        return plfGoodService.listPlfGoods(plfName);
    }
```
> @RequestBody参数定义在DTO内，`@ApiOperation`定义方法名称
``` JAVA
    @PostMapping("testPOSTParamsValid")
    @ApiOperation(value = "POST请求，自定义验证消息")
    public void testParamValid(@RequestBody LoginDTO loginBO) {
        StaticLog.debug("loginBO:{}", loginBO);
    }
```
> 必须添加，@ApiOperation的tags为本次的版本号
``` JAVA
@ApiOperation(value = "用户分页1", tags = DemoApiVersionConstant.VERSION_1_8_7)
```

## 分页、枚举参数
PageParam包含字段：页码、每页数量，可配合排序枚举字段使用
``` JAVA
public ResultWrapper<MyPage<StuentVO>> pageQuery(PageParam page,
                                                   @ApiParam(value = "排序", required = true)
                                                   @NotNull(message = "排序字段不正确")
                                                   @RequestParam StudentSortEnum sort,
                                                   @ApiParam(value = "课程")
                                                   @RequestParam(required = false) CourseEnum course,
                                                   @Validated StudentQueryCriteriaDTO query) {}
```
## 过滤请求参数
> https://doc.xiaominfo.com/knife4j/ignoreParameter.html
## 包含请求参数
> https://doc.xiaominfo.com/knife4j/includeParameter.html
