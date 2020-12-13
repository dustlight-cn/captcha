# tencent-sms
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/tencent-sms?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/tencent-sms?server=https%3A%2F%2Foss.sonatype.org%2F)

## 简介
**tencent-sms** 是 **[CAPTCHA](../../)** 项目的一个拓展，提供短信验证码发送功能（基于 [腾讯云短信服务](https://cloud.tencent.com/product/sms) ）。
使用此拓展需要提供腾讯云短信服务的 
[密钥](https://console.cloud.tencent.com/cam/capi) ，
[SDKAppID](https://console.cloud.tencent.com/smsv2/app-manage) ， 
[模板ID](https://console.cloud.tencent.com/smsv2/csms-template) ， 
[签名](https://console.cloud.tencent.com/smsv2/csms-sign) ，
若您仍未拥有密钥请前往创建。

## 完整示例
> 此示例同时包含 reCAPTCHA v2 以及 reCAPTCHA v3 ，在开始之前请先创建一个 Spring Boot 的 Web 项目，并 [添加依赖](#添加依赖) 。

### 添加依赖
在 Maven 项目的 pom.xml 文件中添加依赖：
```xml
<dependency>
    <groupId>cn.dustlight.captcha</groupId>
    <artifactId>tencent-sms</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
如果版本是以 SNAPSHOT 结尾的快照版本，需要添加 Maven 仓库：
```xml
<repositories>
    <repository>
        <id>sonatype-oss-snapshots</id>
        <name>Sonatype OSS Snapshots Repository</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

### 配置文件
application.yaml:
```yaml
dustlight:
  captcha:
    generator:
      random-string:
        chars: "0123456789" # 随机字符池
        length: 4 # 随机字符验证码长度
    sender:
      tencent:
        sms:
          secret-id: <SECRET_ID> # 腾讯云密钥 ID
          secret-key: <SECRET_KEY> # 腾讯云密钥 Key
          default-app-id: <SDK_APP_ID> # 默认短信 SdkAppID
          default-sign: <SIGN> # 默认短信签名
          default-template-id: <TEMPLATE_ID> # 默认短信模板ID
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RestControllerAdvice
public class TestController {

    Log log = LogFactory.getLog(TestController.class.getName());

    /**
     * 获取图片验证码
     *
     * @param code 生成的验证码
     */
    @RequestMapping("/captcha/image")
    @SendCode
    public void image(@CodeValue String code) {
        log.info(String.format("图片验证码：%s", code));
    }

    /**
     * 获取短信验证码（注解 "@VerifyCode" 表示在此之前先验证图片验证码，注解 "@SendCode" 中的 sender 指定为 "tencentSmsSender" 表示使用腾讯云短信服务）
     *
     * @param code    被验证的图片验证码
     * @param smsCode 生成的短信验证码，注解 '@CodeValue("sms")' 表示短信验证码的名称为 "sms" ，用于区别先前的图片验证码。
     * @param phone   目标手机，注解 "@CodeParam" 表示此值将被存储
     */
    @RequestMapping("/captcha/sms")
    @VerifyCode
    @SendCode(value = "sms", sender = @Sender("tencentSmsSender"))
    public void sms(@CodeValue String code,
                    @CodeValue("sms") String smsCode,
                    @CodeParam("sms") String phone) {
        log.info(String.format("短信验证码：%s，目标手机：%s", smsCode, phone));
    }

    /**
     * 验证短信验证码
     *
     * @param code  短信验证码
     * @param phone 手机号码，从验证码存储器取出的值。
     * @return
     */
    @RequestMapping("/verify")
    @VerifyCode("sms")
    public String verify(@CodeValue("sms") String code, @CodeParam("sms") String phone) {
        log.info(String.format("%s 验证成功，手机号码：%s", code, phone));
        return phone;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus
    public String onException(Exception e) {
        return e.getMessage();
    }
}
```

### 创建前端页面
在 "resources/static" 目录下创建文件 "index.html" ：
```html
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>Tencent SMS 测试</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<header style="padding: 8px;">
    <h1><a href="/">Tencent SMS 测试</a></h1>
</header>
<main style="padding: 16px;">
    <div>
        <h3>1. 发送手机验证码</h3>
        <form>
            <div>
                <label>手机号码：+86</label>
                <input id="phone" type="text" placeholder="请输入您的手机号码">
            </div>
            <div>
                <label>验证码：</label>
                <input id="code" type="text" placeholder="请输入图片中的数字">
                <img id="image" width="100px" height="50px">
            </div>
            <div style="margin: 8px;">
                <input id="sendSms" type="submit" value="发送短信验证码">
            </div>
        </form>
    </div>
    <div style="margin-top: 16px;">
        <h3>2. 验证</h3>
        <form>
            <div>
                <label>短信验证码：</label>
                <input id="sms" type="text" placeholder="请输入您收到的短信验证码">
            </div>
            <div style="margin: 8px;">
                <input id="verify" type="submit" value="验证">
            </div>
        </form>
    </div>
</main>
<script>
    const submitBtn = document.getElementById("sendSms")
    const image = document.getElementById("image")
    const phone = document.getElementById("phone")
    const code = document.getElementById("code")

    const sms = document.getElementById("sms")
    const verifyBtn = document.getElementById("verify")

    function post(url, data) {
        let dataString = "";
        for (let key in data) {
            if (dataString.length > 0) dataString += "&";
            dataString += encodeURIComponent(key) + "=" + encodeURIComponent(data[key])
        }
        return fetch(url, {
            method: "post",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: dataString
        }).then(response => {
            if (response.ok) return response.text(); else return response.text().then(str => {
                throw new Error(str)
            })
        })
    }

    function loadImage() {
        image.setAttribute("src", "/captcha/image?" + new Date().getTime())
    }

    function sendSms(e) {
        e.preventDefault()
        post("/captcha/sms", {code: code.value, phone: "+86" + phone.value})
            .then(response => {
                alert("发送成功！" + response)
                loadImage()
            })
            .catch(e => {
                alert("错误：" + e.message)
                loadImage()
            })
    }

    function verify(e) {
        e.preventDefault()
        post("/verify", {code: sms.value})
            .then(phone => alert("手机:" + phone + ", 验证成功!"))
            .catch(e => alert("错误：" + e.message))
    }

    submitBtn.onclick = sendSms
    verifyBtn.onclick = verify
    image.onclick = loadImage

    loadImage()
</script>
</body>
</html>
```

### 运行
运行您的项目，打开 [http://localhost:8080/](http://localhost:8080/) 进行测试。

## 获取帮助
如果需要报告问题或者功能需求，请在Github中 [创建issue](https://github.com/Hansin1997/captcha/issues/new) 。若有其他问题或建议，请发送电子邮件至 [hansin@dustlight.cn](mailto:hansin@dustlight.cn)