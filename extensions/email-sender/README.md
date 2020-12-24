# email-sender
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/email-sender?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/email-sender?server=https%3A%2F%2Foss.sonatype.org%2F)

## 简介
**email-sender** 是 **[CAPTCHA](../../)** 项目的一个拓展，提供邮箱验证码发送功能。基于 JavaMailSender 实现邮件发送功能，模板引擎为 FreeMaker 。

## 快速入门
### 添加依赖
在 Maven 项目的 pom.xml 文件中添加依赖：
```xml
<dependency>
    <groupId>cn.dustlight.captcha</groupId>
    <artifactId>email-sender</artifactId>
    <version>0.0.4</version>
</dependency>
```

### 启用 CAPTCHA
添加 @EnableCaptcha 注解。
```java
package com.example.demo;

import cn.dustlight.captcha.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCaptcha // 启用 CAPTCHA
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

### 配置邮件服务器
在 application.yaml 添加邮件服务器配置：
```yaml
spring:
  mail:
    host: <SMTP_HOST> # SMTP邮件发送服务器，例如：smtp.exmail.qq.com
    username: <SMTP_USERNAME> # SMTP 用户名
    password: <SMTP_PASSWORD> # SMTP 密码
```

### 创建邮件模板
在 resources 目录创建文件 sendCode.html ：
```html
<html>
<body>
您的验证码为： ${code}
</body>
</html>
```

### 创建 Controller
创建 EmailController.java ：
```java
package com.example.demo;

import cn.dustlight.captcha.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class EmailController {

    /**
     * 发送邮箱验证码
     * <p>
     * 注解 '@Parameter' 为邮件发送器提供 邮件主题 以及 模板名。
     *
     * @param code  生产的验证码（此参数由验证码生成器传入并自动储存）
     * @param email 目标邮箱（此参数为 @RequestParam ，注解 @CodeParam 表示此参数将与验证码一起储存，以便验证成功后取出）
     */
    @RequestMapping("/email")
    @SendCode(sender = @Sender("emailCodeSender"),
            parameters = {
                    @Parameter(name = "SUBJECT", value = "邮箱验证"),
                    @Parameter(name = "TEMPLATE", value = "sendCode.html")
            })
    public String sendEmailCode(@CodeValue String code, @CodeParam String email) {
        Logger.getGlobal().info(code);
        return "send code success";
    }

    /**
     * 进行验证
     *
     * @param code  被验证的验证码（此参数为 @RequestParam）
     * @param email 验证成功后，传入储存的 email 参数。
     * @return
     */
    @RequestMapping("/verify")
    @VerifyCode
    public String verify(@CodeValue String code, @CodeParam String email) {
        Logger.getGlobal().info(email);
        return String.format("verify email '%s' success", email);
    }
}
```

### 运行
运行您的项目，打开 [http://localhost:8080/email?email=xx@xx.com](http://localhost:8080/email?email=xx@xx.com) 进行测试。（将 xx@xx.com 改为您的电子邮箱） 
收到验证码后，打开 [http://localhost:8080/verify?code=xx](http://localhost:8080/verify?code=xx) 进行验证。（将 xx 改为您收到的验证码）

## 获取帮助
如果需要报告问题或者功能需求，请在Github中 [创建issue](https://github.com/dustlight-cn/captcha/issues/new) 。若有其他问题或建议，请发送电子邮件至 [hansin@dustlight.cn](mailto:hansin@dustlight.cn)
## 常见问题
### 验证码参数获取失败
发送邮件时，模板引擎无法找到参数：
```
----
FTL stack trace ("~" means nesting-related):
        - Failed at: ${code}  [in template "mail/EmailCode.html" at line 8, column 12]
----
        at freemarker.core.InvalidReferenceException.getInstance(InvalidReferenceException.java:134) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.core.EvalUtil.coerceModelToTextualCommon(EvalUtil.java:481) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.core.EvalUtil.coerceModelToStringOrMarkup(EvalUtil.java:401) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.core.EvalUtil.coerceModelToStringOrMarkup(EvalUtil.java:370) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.core.DollarVariable.calculateInterpolatedStringOrMarkup(DollarVariable.java:100) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.core.DollarVariable.accept(DollarVariable.java:63) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.core.Environment.visit(Environment.java:334) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.core.Environment.visit(Environment.java:340) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.core.Environment.process(Environment.java:313) ~[freemarker-2.3.30.jar!/:2.3.30]
        at freemarker.template.Template.process(Template.java:383) ~[freemarker-2.3.30.jar!/:2.3.30]
        at org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString(FreeMarkerTemplateUtils.java:50) ~[spring-context-support-5.2.10.RELEASE.jar!/:5.2.10.RELEASE]
        at cn.dustlight.captcha.sender.EmailCodeSender$DefaultTemplateProvider.getTemplateContent(EmailCodeSender.java:100) ~[email-sender-0.0.4.jar!/:0.0.4]
        at cn.dustlight.captcha.sender.EmailCodeSender.getTemplate(EmailCodeSender.java:76) ~[email-sender-0.0.4.jar!/:0.0.4]
        at cn.dustlight.captcha.sender.EmailCodeSender.send(EmailCodeSender.java:39) ~[email-sender-0.0.4.jar!/:0.0.4]
        ... 101 common frames omitted
```
原因为 Java 在编译时处于安全等原因考虑，方法参数名并非是原参数名，通常会更改成 arg0, arg1, arg2... 等形式。
在编译时添加参数 '-parameters' 即可保留原参数名。例如：```javac -parameters XX.java```

在 Maven 项目的编译选项添加配置 ```<arg>-parameters</arg>```：
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.3</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
        <compilerArgs>
            <arg>-parameters</arg>
        </compilerArgs>
    </configuration>
</plugin>
```