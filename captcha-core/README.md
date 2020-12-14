# captcha-core
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/captcha-core?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/captcha-core?server=https%3A%2F%2Foss.sonatype.org%2F)

## 简介
**captcha-core** 是 **[CAPTCHA](../)** 项目的核心模块，提供验证码业务的抽象接口、注解以及一些默认实现。

## 核心组件
* 抽象接口
  * [验证码 - Code](src/main/java/cn/dustlight/captcha/core/Code.java)
  * [验证码生成器 - CodeGenerator](src/main/java/cn/dustlight/captcha/generator/CodeGenerator.java)
  * [验证码储存器 - CodeStore](src/main/java/cn/dustlight/captcha/store/CodeStore.java)
  * [验证码发送器 - CodeSender](src/main/java/cn/dustlight/captcha/sender/CodeSender.java)
  * [验证码校验器 - CodeVerifier](src/main/java/cn/dustlight/captcha/verifier/CodeVerifier.java)
* 核心注解
  * [启用CAPTCHA - @EnableCaptcha](src/main/java/cn/dustlight/captcha/annotations/EnableCaptcha.java)
  * [发送验证码 - @SendCode](src/main/java/cn/dustlight/captcha/annotations/SendCode.java)
  * [校验验证码 - @VerifyCode](src/main/java/cn/dustlight/captcha/annotations/VerifyCode.java)

## 默认实现
### 验证码
[DefaultCode](src/main/java/cn/dustlight/captcha/core/DefaultCode.java) 是验证码接口的默认实现，它是一个泛型类。

### 生成器
[RandomStringCodeGenerator](src/main/java/cn/dustlight/captcha/generator/RandomStringCodeGenerator.java) 是验证码发送器的默认实现，负责生成随机字符串验证码，可以设置生成验证码的字符与长度。

### 储存器
[HttpSessionCodeStore](src/main/java/cn/dustlight/captcha/store/HttpSessionCodeStore.java) 是验证码储存器的默认实现，通过 HttpSession 储存验证码并为其设置有效期。

### 发送器
[SimpleImageCodeSender](src/main/java/cn/dustlight/captcha/sender/SimpleImageCodeSender.java) 是验证码发送器的默认实现，它将验证码的值作为字符串画到画板并生成干扰条纹，最终将生成的图像输出。

### 校验器
[StringCodeVerifier](src/main/java/cn/dustlight/captcha/verifier/StringCodeVerifier.java) 是验证码校验器的默认实现，它将验证码值作为字符串进行比较，可以选择是否裁剪空白以及大小写敏感。

## 获取帮助
如果需要报告问题或者功能需求，请在Github中 [创建issue](https://github.com/Hansin1997/captcha/issues/new) 。若有其他问题或建议，请发送电子邮件至 [hansin@dustlight.cn](mailto:hansin@dustlight.cn)