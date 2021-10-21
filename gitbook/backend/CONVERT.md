# 类型转换

## Java常见类型转换
https://hutool.cn/docs/#/core/%E7%B1%BB%E5%9E%8B%E8%BD%AC%E6%8D%A2/%E7%B1%BB%E5%9E%8B%E8%BD%AC%E6%8D%A2%E5%B7%A5%E5%85%B7%E7%B1%BB-Convert

## MapStruct
### DO,VO映射
``` JAVA
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapStruct extends BaseMapStruct<StudentDO, StudentDTO, StudentVO> {
}

StudentDO studentDO = this.studentMapStruct.toDO(dto);
```



## ~~oriKaMapper备用~~

### ~~pojo转换~~
``` JAVA
public List<StudentVO> listStudentsByName(String uName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StrUtil.isNotBlank(uName)) {
            Map map = MapUtil.newHashMap();
            map.put("name", uName);
            queryWrapper.allEq(map);
        }
        List<StudentDO> list = this.list(queryWrapper);
        return oriKaMapper.mapAsList(list, StudentVO.class);
    }
```
### ~~自定义类型转换~~
> 1. 定义转换器，不同类型或不同属性名的手动set
```JAVA
/**
 * <p>
 * 测试自定义转换
 * </p>
 *
 * @author miaoyj
 * @version 1.0.9-SNAPSHOT
 * @since 2020-07-09
 */
@Component
public class LoginDtoToLoginVoMapper extends CustomMapper<LoginDTO, LoginVO> {
    /** {@inheritDoc} */
    @Override
    public void mapAtoB(LoginDTO loginDTO, LoginVO loginVO, MappingContext context) {
        loginVO.setName(loginDTO.getNickname());
        super.mapAtoB(loginDTO, loginVO, context);
    }

    /** {@inheritDoc} */
    @Override
    public void mapBtoA(LoginVO loginVO, LoginDTO loginDTO, MappingContext context) {
        loginDTO.setNickname(loginVO.getName());
        super.mapBtoA(loginVO, loginDTO, context);
    }
}
```
> 2. ~~调用~~
``` JAVA
@Autowired
private OriKaMapper oriKaMapper;

@Override
    public LoginVO testOriKaMapper(LoginDTO loginDTO) {
        return oriKaMapper.map(loginDTO, LoginVO.class);
    }
```
