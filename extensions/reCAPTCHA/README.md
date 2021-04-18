# reCAPTCHA
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/recaptcha?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/recaptcha?server=https%3A%2F%2Foss.sonatype.org%2F)

## 简介
**reCAPTCHA** 是 **[CAPTCHA](../../)** 项目的一个拓展，提供基于谷歌 [reCAPTCHA](https://www.google.com/recaptcha/about/) 人机识别的验证服务。
使用此拓展需要提供 reCAPTCHA 服务器端密钥，若您仍未拥有密钥请 [前往创建](https://www.google.com/recaptcha/admin/create) 。

## 快速入门
### 添加依赖
在 Maven 项目的 pom.xml 文件中添加依赖：
```xml
<dependency>
    <groupId>cn.dustlight.captcha</groupId>
    <artifactId>recaptcha</artifactId>
    <version>0.0.7</version>
</dependency>
```

### 添加并配置注解
为需要被保护的接口方法添加注解 "@VerifyCode" ，并为其配置相关组件、密钥参数：
```java
/**
 * reCAPTCHA v3 验证
 * <p>
 * "reCaptchaV3Store" 为 reCAPTCHA v3 的存储器。
 * 注解 ”@Parameter“ 提供了名为 "SECRET" 的参数，值为 reCAPTCHA 的服务端密钥。未指定此参数时将从配置文件加载默认的服务的密钥。
 *
 * @param result 验证通过后，传入详细信息
 * @return
 */
@RequestMapping("/checkV3")
@VerifyCode(store = @Store("reCaptchaV3Store"),
        verifier = @Verifier("reCaptchaVerifier"),
        parameters = {
                @Parameter(name = "SECRET", value = "6LfDVwQaAAAAALitGSeXauEgdVgaiKQEU1S_hwfj")
        }
)
public String v3(@CodeValue ReCaptchaV3Result result) {
    log.info(result.toString());
    return String.format("Hello World! <br> (%s)", result);
}
```
通过以上配置，您的接口已经受到了 reCAPTCHA v3 的保护，参考 [谷歌 reCAPTCA 文档](https://developers.google.com/recaptcha/docs/v3) 对前端进行配置后即可进行访问。


## 完整示例
> 此示例同时包含 reCAPTCHA v2 以及 reCAPTCHA v3 ，在开始之前请先创建一个 Spring Boot 的 Web 项目，并 [添加依赖](#添加依赖) 。

### 配置文件
application.yaml:
```yaml
dustlight:
  captcha:
    recaptcha:
      default-secret: 6LdJdwQaAAAAALBczp1QblD29uiVggPB19XBEChD # 默认的服务端密钥
      # endpoint: "https://www.recaptcha.net/recaptcha/api/siteverify" # API URL
      # default-parameter-name: "g-recaptcha-response" # 默认的参数名（传入 reCAPTCHA 的 token）
```

### 启用 CAPTCHA
添加注解 "@EnableCaptcha" ：
```java
package com.example.demo;

import cn.dustlight.captcha.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCaptcha // 开启 CAPTCHA
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

### 创建 Controller
创建 "TestController.java" :
```java
package com.example.demo;

import cn.dustlight.captcha.annotations.*;
import cn.dustlight.captcha.recaptcha.ReCaptchaResult;
import cn.dustlight.captcha.recaptcha.ReCaptchaV3Result;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {

    private final Log log = LogFactory.getLog(TestController.class.getSimpleName());

    /**
     * reCAPTCHA v2 验证
     * <p>
     * 通过配置注解 @VerifyCode 中的参数 store 和 verifier 来指定储存器和验证器。
     * "reCaptchaStore" 为 reCAPTCHA v2 的存储器。
     *
     * @param result 验证通过后，传入详细信息
     * @return
     */
    @RequestMapping("/checkV2")
    @VerifyCode(store = @Store("reCaptchaStore"), verifier = @Verifier("reCaptchaVerifier"))
    public String index(@CodeValue ReCaptchaResult result) {
        log.info(result.toString());
        return String.format("Hello World! <br> (%s)", result);
    }

    /**
     * reCAPTCHA v3 验证
     * <p>
     * "reCaptchaV3Store" 为 reCAPTCHA v3 的存储器。
     * 注解 ”@Parameter“ 提供了名为 "SECRET" 的参数，值为 reCAPTCHA 的服务端密钥。未指定此参数时将从配置文件加载默认的服务的密钥。
     *
     * @param result 验证通过后，传入详细信息
     * @return
     */
    @RequestMapping("/checkV3")
    @VerifyCode(store = @Store("reCaptchaV3Store"),
            verifier = @Verifier("reCaptchaVerifier"),
            parameters = {
                    @Parameter(name = "SECRET", value = "6LfDVwQaAAAAALitGSeXauEgdVgaiKQEU1S_hwfj")
            }
    )
    public String v3(@CodeValue ReCaptchaV3Result result) {
        log.info(result.toString());
        return String.format("Hello World! <br> (%s)", result);
    }
}
```

### 创建前端页面
#### reCAPTCHA v2 
在 "resources/static" 目录下创建文件 "v2.html" ：
```html
<!DOCTYPE html>
<html lang="zh_cn">
<head>
    <title>reCAPTCHA v2 测试</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<main>
    <header>
        <h1>reCAPTCHA v2 测试</h1>
    </header>
    <main>
        <form action="/checkV2" method="POST">
            <div class="g-recaptcha" data-sitekey="6LdJdwQaAAAAALh13PuEzS7u53nlHIueQC1QH2f7"></div>
            <input type="submit" value="提交表单">
        </form>
    </main>
    <footer style="margin-top: 100px;">
        <a href="/v3.html">reCAPTCHA v3 测试</a>
    </footer>
</main>
<script src="https://www.recaptcha.net/recaptcha/api.js" async defer></script>
</body>
</html>
```

#### reCAPTCHA v3
在 "resources/static" 目录下创建文件 "v3.html" ：

```html
<!DOCTYPE html>
<html lang="zh_cn">
<head>
    <title>reCAPTCHA v3 测试</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<main>
    <header>
        <h1>reCAPTCHA v3 测试</h1>
    </header>
    <main>
        <form id="demo-form" ) action="/checkV3" method="POST">
            <button class="g-recaptcha"
                    data-sitekey="6LfDVwQaAAAAAMMlJwOrV0bc6Vv-HGewhCiZzh1X"
                    data-callback='onSubmit'
                    data-action='social'>Submit
            </button>
        </form>
    </main>
    <footer style="margin-top: 100px;">
        <a href="v2.html">reCAPTCHA v2 测试</a>
    </footer>
</main>
<script src="https://www.recaptcha.net/recaptcha/api.js"></script>
<script>
    function onSubmit(token) {
        document.getElementById("demo-form").submit();
    }
</script>
</body>
</html>
```

### 运行
运行您的项目并进行测试：

| URL | 描述 |
| ---- | ---- |
| [http://localhost:8080/v2.html](http://localhost:8080/v2.html) | reCAPTCHA v2 测试页面 |
| [http://localhost:8080/v3.html](http://localhost:8080/v3.html) | reCAPTCHA v3 测试页面 |

## 获取帮助
如果需要报告问题或者功能需求，请在Github中 [创建issue](https://github.com/dustlight-cn/captcha/issues/new) 。若有其他问题或建议，请发送电子邮件至 [hansin@dustlight.cn](mailto:hansin@dustlight.cn)