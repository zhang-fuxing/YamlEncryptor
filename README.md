# YamlEncryptor
spring boot 配置文件加密 密码生成工具
## spring boot配置文件加密简单使用
1. 使用maven 添加maven依赖
```xml
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>
```
2. yml、yaml、properties配置文件中添加配置
使用yml或者yaml
```yml
# 配置文件加密、解密需要的串
jasypt:
  encryptor:
    # 这属性是自定义的，生成密码的时候必须要用和这个一样的字符串（zheng），不然无法解密
    password: zhen
```
使用properties
```properties
jasypt.encryptor.password=zhen
```
接下来就可以在配置文件中使用密文了，但是，需要使用 `ENC()` 包裹密码。当然，可以自定义，不过我懒得动了
例如：
```yml
spring:
  datasource:
    # 数据源基本配置
    driver-class-name: oracle.jdbc.OracleDriver
    type: com.alibaba.druid.pool.DruidDataSource
    dynamic:
      primary: master
      strict: true
      master:
        username: root
        # 1234567890
        password: ENC(J9Gp9MKgX28CyXH7f8xrZbvmZIcaNHt8SONGHTSj4s2Pdd9xSwX2j+1fwx1JgVUq)
        url: jdbc:mysql://localhost:3306*********
```
如上这段配置，对 `password` 字段进行了加密，但是注入到spring boot项目中时，会被自动解密。

本项目是生成如上密文的密码生成工具，当然，都是使用的默认方式。
将本项目打包为.jar文件后，可以使用 `java -jar xxx.jar 明文1 明文2 ... [/,文件路径]` 的命令生成密文，生成的密文已经使用 `ENC()` 包裹了
最后的 `/` 表示在控制台输出加密后的密文，如果不适用 `/` 而是使用 `./code.txt` 类似的文件路径，那么密文不会在控制台打印，而是会在指定的文件中生成
生成到文件使用的是覆盖写，也就是原本的文件内容会被清空，请注意这点。
