# 配置文件敏感信息加密

> application.yml
`pwd: ENC(加密后的字段)`

> 启动jar
java -jar demo.jar --jasypt.encryptor.password=${password}


###  使用jasypt加解密
``` JAVA
    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("G0CvDz7oJn6");
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("root");
        String password = textEncryptor.encrypt("root123");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
    }
```
### password(盐)，input(加密内容)
> 加密
` $ java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.3/jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI password=e9fbdb2d3b21 algorithm=PBEWithMD5AndDES input= `

> 解密
` $ java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.3/jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringDecryptionCLI  password=e9fbdb2d3b21 algorithm=PBEWithMD5AndDES input= `
