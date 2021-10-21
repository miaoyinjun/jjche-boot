# 注释规范
### 开发工具：IDEA



### 单行注释 

``` JAVA
/**
* TODO
*/
/** TODO */
// TODO
```
### JAVA文件-注释模板配置
![image-20210916161620705](../assets/image-20210916161620705.png)

1. 菜单Preferences/Editor/Live Templates

2. 选择顶部 By default expand with **Enter**

3. 新建Template Group，名称：**user**

4. 勾选 Reformat according to style

5. 并在user下新建Live Template为**cc**填入以下注释模板

   
``` JAVA
* 
 * <p>
 * $END$
 * </p>
 *            
 * @author $user$
 * @since $date$
 */
```


* Change作用范围勾选java
* Edit variables的Default Value列设置如下
> date = date("yyyy-MM-dd")
> user = user()

* 打开一个java文件，在**public**上方输入 **/cc**，按**回车键**，即可生成注释，**填入标题**
生成示例：
```JAVA
/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @since 2020-07-09
 */
```



### JAVA方法-注释模板

![image-20210916161643677](../assets/image-20210916161643677.png)

1. 菜单Preferences/Editor/Live Templates
2. 选择顶部 By default expand with **Enter**
3. 新建Template Group，名称：**user**
4. 勾选 Reformat according to style
5. 并在user下新建Live Template为**mc**填入以下注释模板

``` JAVA
*
 * <p>
 * $END$
 * </p>  
 *
$params$$return$
 */
```

* Change作用范围勾选java
* Edit variables的Default Value列设置如下
``` JAVA
    params = groovyScript("def result=''; def params=\"${_1}\".replaceAll('[\\\\[|\\\\]|\\\\s]', '').split(',').toList(); for(i = 0; i < params.size(); i++) {if(params[i] == '') return result;result+=' * @param ' + params[i] + ((i < params.size() - 1) ? ' \\n' : '')}; return result", methodParameters());
    return = groovyScript("def returnType = \"${_1}\"; def params=\"${_2}\".replaceAll('[\\\\[|\\\\]|\\\\s]', '').split(',').toList(); def result = '* @return /'; if(params.size() > 0 && params[0] != '') {result='\\n'+result};if(!returnType.toString().equals('void')){return result;};", methodReturnType(), methodParameters());
    date = date("yyyy-MM-dd")
    user = user()
    time = time("HH:mm")
```
* 在方法上方输入 /*mc + Enter，即可生成注释，*填入标题、参数描述、返回描述*
生成示例：
```JAVA
    /**
     * <p>
     * 根据商品名称查询列表
     * </p>
     *
     * @param plfName 商品名称
     * @return 商品列表
     */
```
### 常量注释模板
生成示例：
```JAVA
/** 开发 {@value} */
String DEV = "dev";
```

### 子类或继承方法不需要增加注释，在其上增加：
``` JAVA
/** {@inheritDoc} */
```
