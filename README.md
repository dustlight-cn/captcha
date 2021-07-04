# CAPTCHA
[![Build Status](https://travis-ci.org/dustlight-cn/captcha.svg?branch=main)](https://travis-ci.org/dustlight-cn/captcha) 
[![GitHub](https://img.shields.io/github/license/dustlight-cn/captcha)](LICENSE)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/dustlight-cn/captcha)](https://github.com/dustlight-cn/captcha/releases)

## 简介
**CAPTCHA** 是一个基于 **Spring Boot** 框架的验证码框架，它通过 AOP 的方式完成包含验证码生成、发送、存储等验证码相关业务，以避免与业务代码耦合。
开发者可以轻松地通过不同组件的组合来完成验证业务，同时可以进行自定义实现以应对自身的业务需求（例如邮箱验证码、短信验证码）。

## 示例
* [图片验证码](#快速入门)
* [腾讯云短信验证码](extensions/tencent-sms) / [阿里云短信验证码](extensions/aliyun-sms)
* [邮箱验证码](extensions/email-sender)
* [谷歌 reCAPTCHA](extensions/reCAPTCHA)
* [使用 Redis 储存验证码](extensions/redis-store)

## 目录
* [简介](#简介)
* [示例](#示例)
* [快速入门](#快速入门)
* [原理](#原理)
* [核心模块](#核心模块)
* [拓展模块](#拓展模块)
* [获取帮助](#获取帮助)
* [常见问题](#常见问题)

## 快速入门
> 下面将演示如何实现字符型图片验证码业务，在开始之前请先创建一个 Spring Boot 的 Web 项目。

### 添加依赖
在 Maven 项目的 pom.xml 文件中添加依赖：
```xml
<dependency>
    <groupId>cn.dustlight.captcha</groupId>
    <artifactId>captcha-core</artifactId>
    <version>0.0.8</version>
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

### 创建 Controller
创建 TestController.java 并创建两个方法分别用于生产验证码和消费验证码。
```java
package com.example.demo;

import cn.dustlight.captcha.annotations.CodeValue;
import cn.dustlight.captcha.annotations.SendCode;
import cn.dustlight.captcha.annotations.VerifyCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final Log log = LogFactory.getLog(TestController.class.getSimpleName());

    /**
     * 获取验证码。
     * <p>
     * 方法的注解 '@SendCode' 表示此方法执行之前将生成验证码并进行发送。
     *
     * @param code 生成的验证码，通过注解 '@CodeValue' 传入
     */
    @RequestMapping("/captcha")
    @SendCode
    public void captcha(@CodeValue String code) {
        // 在此处进行自定义的业务，验证码的生成、发送与储存已由注解 '@SendCode' 完成。
        log.info(code);
    }

    /**
     * 消费验证码
     * <p>
     * 方法的注解 '@VerifyCode' 表示此方法执行之前先进行验证码验证。
     *
     * @param code 参数中的验证码，通过注解 '@CodeValue' 表示此参数为需要被验证的参数。
     * @return
     */
    @RequestMapping("/")
    @VerifyCode
    public String index(@CodeValue String code) {
        // 在此处进行自定义的业务，验证码的验证以及销毁已由注解 '@VerifyCode' 完成。
        return String.format("Hello World! (%s)", code);
    }
}
```
在访问注解 @SendCode 所标注的方法 captcha 之前，会生成随机字符串验证码并以图片形式发送。
访问注解 @VerifyCode 所标注的方法 index 前会对参数中的验证码进行验证，成功验证后才会执行 index 方法的业务。

## 原理
**CAPTCHA** 基于面向切面编程（AOP）思想，将验证码业务划分为两个切面：
- @SendCode
    1. 生成验证码
    2. 储存验证码
    3. 发送验证码
- @VerifyCode
    1. 读取验证码
    2. 进行验证

同时将验证码业务抽象为下面几个接口：
* Code —— 验证码
* CodeGenerator —— 生成器
* CodeSender —— 发送器
* CodeStore —— 储存器
* CodeVerifier —— 验证器

## 核心模块
### [captcha-core](captcha-core)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/captcha-core?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/captcha-core?server=https%3A%2F%2Foss.sonatype.org%2F)

定义各种注解、接口、异常类，并提供默认实现类。

## 拓展模块
### [redis-store](extensions/redis-store)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/redis-store?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/redis-store?server=https%3A%2F%2Foss.sonatype.org%2F)

提供基于 Redis 的验证码存储功能。

### [tencent-sms](extensions/tencent-sms)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/tencent-sms?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/tencent-sms?server=https%3A%2F%2Foss.sonatype.org%2F)

提供基于腾讯云短信服务的验证码发送功能。

### [aliyun-sms](extensions/aliyun-sms)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/aliyun-sms?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/aliyun-sms?server=https%3A%2F%2Foss.sonatype.org%2F)

提供基于阿里云短信服务的验证码发送功能。

### [reCAPTCHA](extensions/reCAPTCHA)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/recaptcha?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/recaptcha?server=https%3A%2F%2Foss.sonatype.org%2F)

集成谷歌 reCAPTCHA 人机识别服务。

### [email-sender](extensions/email-sender)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/email-sender?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/email-sender?server=https%3A%2F%2Foss.sonatype.org%2F)

集成邮箱验证码发送功能。

## 获取帮助
如果需要报告问题或者功能需求，请在Github中 [创建issue](https://github.com/dustlight-cn/captcha/issues/new) 。若有其他问题或建议，请发送电子邮件至 [hansin@dustlight.cn](mailto:hansin@dustlight.cn)

## 常见问题
[邮箱验证码参数获取失败](extensions/email-sender#验证码参数获取失败)