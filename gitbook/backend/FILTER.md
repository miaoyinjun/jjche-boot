# 接口/字段加密

## 字段加/解密
> AES入参解密、出参加密

### 注意事项
>参与加解密的字段类型必须是String
支持的类型有：String、List<String>、POJO、List<POJO>
MyPage仅支持出参类型
不会处理嵌套list

### 注解
>@EncryptMethod，参与加/解密的方法上必须增加，参数returnVal默认false不加密出参，为true时加密出参
>@EncryptField 方法的参数，对象的字段上增加


> 入参示例
```JAVA
    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param id ID
     * @return StudentVO 对象
     */
    @EncryptMethod
    public StudentVO getVoById(@EncryptField String id, @EncryptField List<String> ids, @EncryptField StudentDTO dto, @EncryptField List<StudentDTO> dtos) {
        StudentDO studentDO = this.getById(id);
        Assert.notNull(studentDO, "记录不存在或权限不足");
        return this.studentMapStruct.toVO(studentDO);
    }
```

```JAVA
@Data
public class StudentVO implements Serializable {
   @ApiModelProperty(value = "姓名")
   @EncryptField
   private String name;
}   
```

>出参示例
```JAVA
    @EncryptMethod(returnVal = true)
    public MyPage<StudentVO> pageQuery(PageParam page, StudentSortEnum sort, StudentQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        return this.baseMapper.pageQuery(page, sort, queryWrapper);
    }
```

>非注解处理
> 可使用EncryptFieldUtil类单独使用
> AES加密配置属性boot.admin.filter.encryption.field.secret-key

## 接口加密

> md5与rsa均使用的是验签的方式进行验证，不涉及数据加密

~~TODO 防止重复提交（批量，注解）~~

### 加密类型
1. MD5
> 请求头，必传

|  参数          | 说明         |
|  :---:        | :---:        |     
|  appId        | 应用标识      |
|  timestamp    | 时间戳(毫秒)  |
|  nonce        | 随机数(10位)  |
|  sign         | 签名         |
> sign说明
>服务端会给出一个appId和一个appKey, appKey用于参数签名使用，注意appKey保存到客户端，需要做一些安全处理，防止泄露
>sign的值一般是将URL所有非空参数按照字母升序排序+appKey+timestamp+nonce拼接在一起，然后使用md5生成16进制小写字符串进行加密
>公式：md5生成16进制小写字符串(ASCII升序排序URL所有参数(空参数不参与)+appKey+timestamp+nonce)


2. RSA
>使用各自语言对应的SHA256WithRSA签名函数利用商户私钥对待签名字符串进行签名，并进行Base64编码
>公式：Base64.encode(RSA私钥签名(ASCII排序URL参数(空参数不参与)+timestamp+nonce))
``` JAVA
String str = "我是一段测试aaaa";

        KeyPair keyPair = SecureUtil.generateKeyPair(AsymmetricAlgorithm.RSA.getValue(), 2048);
        byte[] publicKey = keyPair.getPublic().getEncoded();
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        StaticLog.info("publicKey:{}", Base64.encode((publicKey)));
        StaticLog.info("privateKey:{}", Base64.encode(privateKey));

        RSA rsa = SecureUtil.rsa(privateKey, publicKey);

        //公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(str, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
//        StaticLog.info("encrypt:{}", Base64.encode(encrypt));
//        StaticLog.info("decrypt:{}", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));


        //私钥加密，公钥解密
//        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes(str, CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
//        byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
//        StaticLog.info("encrypt2:{}", Base64.encode(encrypt2));
//        StaticLog.info("decrypt2:{}", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));

//        Signature signature = SecureUtil.generateSignature(AsymmetricAlgorithm.RSA, DigestAlgorithm.SHA256);

        byte[] data = str.getBytes();
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA);
        sign.setPrivateKey(keyPair.getPrivate());
        //签名
        byte[] signed = sign.sign(data);
        //验证签名
        sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA);
        sign.setPublicKey(keyPair.getPublic());
        boolean verify = sign.verify(data, signed);
        StaticLog.info("verify:{}", verify);
```


### 配置
> 前缀 boot.admin.filter.encryption.

|  参数           | 说明                            | 默认值                   | 举例            |
|  :---:         | :---:                           | :---:                   | :---:           |
| filter-urls    | 要过滤的地址，数组                 | @APIDefineField值+/**   |  - /demo/**     |
| exclude-urls   | 要忽略的地址，数组                 | 无                      | - /demo/students/** |
| type           | 加密方式(MD5|RSA)                | MD5                     |
| enabled        | 是否启用(true|false)             | true                    |
| timestamp      | 超时时效，超过此时间认为签名过期毫秒 | 5 * 60 * 1000L          | 5 * 60 * 1000L   |
| nonce-length    | 随机数长度                       | 10                      | 5L               |
| applications   | 客户端信息(appId:appKey)          | 无                      | - id: 2 key: 22 privateKey:  publicKey          |
