# 枚举定义
> 入参使用限制
1. 定义在类里，只支持@RequestBody
2. 定义在控制器里，只支持@RequestParam
3. mybatis如何使用枚举，[mybatis](MYBATIS.md)


> ENUM(枚举类)，这样定义的好处是swagger与springMVC的出入参都是value


```JAVA
    @Getter
    @AllArgsConstructor
    public enum CourseEnum {
    
        /**
         * 图文
         */
        PICTURE("102", "图文"),
        /**
         * 音频
         */
        AUDIO("103", "音频"),
        /**
         * 视频
         */
        VIDEO("104", "视频"),
        /**
         * 外链
         */
        URL("105", "外链"),
        ;
    
        @JsonValue
        @EnumValue
        private final String value;
        private final String desc;
    
        private static final Map<String, CourseEnum> MAPPINGS;
    
        static {
            Map<String, CourseEnum> temp = new HashMap<>();
            for (CourseEnum courseEnum : values()) {
                temp.put(courseEnum.value, courseEnum);
            }
            MAPPINGS = Collections.unmodifiableMap(temp);
        }
    
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static CourseEnum resolve(String index) {
            return MAPPINGS.get(index);
        }
    }
```
> 字段排序枚举


``` JAVA
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
     * @param index a String.
     * @return 枚举
     * @author miaoyj
     * @since 2020-10-19
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StudentSortEnum resolve(String index) {
    return MAPPINGS.get(index);
    }
}
```

> 使用
```
/**
     * <p>
     * 测试enum
     * </p>
     *
     * @param courseEnum 课程类型
     * @return 登录信息
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("enum")
    @ApiOperation(value = "测试enum")
    public LoginVO wd(@ApiParam(value = "排序", required = true)
                      @NotNull(message = "排序字段不正确")
                      @RequestParam StudentSortEnum sort,
                      @ApiParam(value = "课程")
                      @RequestParam(required = false) CourseEnum course) {
            LoginVO loginVO = new LoginVO();
            loginVO.setCourseEnum(course);
            return loginVO;
        }
```
> 排序枚举，配合mybatisplus排序使用，mapper接口定义sort字段，orderHelper插件会在sql里自动加入order by 
```
public IPage<StudentVO> pageVo(Integer pageIndex, Integer pageSize,
                                String name, CourseEnum courseEnum,
                                StudentSortEnum sort) {
        Page<StudentVO> page = new Page(pageIndex, pageSize);
        page.addOrder(OrderItem.desc(studentOrderColumnEnum.getValue()));
        return this.baseMapper.pageVo(page, name, sort);
    }
```



## 实现效果

![enum1](../assets/enum1.png)

![enum2](../assets/enum2.png)

![enum3](../assets/enum3.png)
