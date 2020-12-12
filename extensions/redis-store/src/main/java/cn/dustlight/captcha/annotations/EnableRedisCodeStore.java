package cn.dustlight.captcha.annotations;

import cn.dustlight.captcha.RedisCodeStoreConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RedisCodeStoreConfiguration.class})
@Deprecated
public @interface EnableRedisCodeStore {

}
