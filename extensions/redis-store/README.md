# redis-store
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/r/cn.dustlight.captcha/redis-store?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/cn.dustlight.captcha/redis-store?server=https%3A%2F%2Foss.sonatype.org%2F)

## 简介
**redis-store** 是 **[CAPTCHA](../../)** 项目的一个拓展，提供基于 Redis 的验证码存储服务。

## 添加依赖
在 Maven 项目的 pom.xml 文件中添加依赖：
```xml
<dependency>
    <groupId>cn.dustlight.captcha</groupId>
    <artifactId>redis-store</artifactId>
    <version>0.0.6</version>
</dependency>
```

## 配置全局默认
> 全局默认使用 Redis 进行验证码存储。

配置 application.yaml: 
```yaml
# 配置 Redis
spring:
  redis:
    host: <REDIS_HOST> # Redis Host
    port: <REDIS_PORT> # Redis Port

# 配置 CAPTCHA
dustlight:
  captcha:
    default:
      store:
        name: "redisCodeStore" # 设置默认验证码存储器（Bean名称）
    store:
      redis:
        key-prefix: "CAPTCHA_CODE" # Redis 存储的 Key 前缀
```
或者配置 application.properties:
```properties
# 配置 Redis
spring.redis.host=<REDIS_HOST>
spring.redis.port=<REDIS_PORT>

# 配置 CAPTCHA
dustlight.captcha.default.store.name=redisCodeStore
dustlight.captcha.store.redis.key-prefix=CAPTCHA_CODE
```

完成配置之后，@SendCode 与 @VerifyCode 默认使用 redisCodeStore 作为验证码存储器。

## 配置局部使用
> 不想全局使用 Redis 存储，而希望在指定方法使用 Redis 存储。

配置 application.yaml: 
```yaml
# 配置 Redis
spring:
  redis:
    host: <REDIS_HOST> # Redis Host
    port: <REDIS_PORT> # Redis Port

# 配置 CAPTCHA
dustlight:
  captcha:
    store:
      redis:
        key-prefix: "CAPTCHA_CODE" # Redis 存储的 Key 前缀
```
或者配置 application.properties:
```properties
# 配置 Redis
spring.redis.host=<REDIS_HOST>
spring.redis.port=<REDIS_PORT>

# 配置 CAPTCHA
dustlight.captcha.store.redis.key-prefix=CAPTCHA_CODE
```

通过删除配置项 'dustlight.captcha.default.store.name' 即可取消全局默认配置，在需要使用 Redis 进行储存和获取时可以通过注解参数 'store' 指定：
```java
@SendCode(store = @Store("redisCodeStore"))
```
```java
@VerifyCode(store = @Store("redisCodeStore"))
```

## 获取帮助
如果需要报告问题或者功能需求，请在Github中 [创建issue](https://github.com/dustlight-cn/captcha/issues/new) 。若有其他问题或建议，请发送电子邮件至 [hansin@dustlight.cn](mailto:hansin@dustlight.cn)