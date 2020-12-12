package cn.dustlight.captcha;

import cn.dustlight.captcha.store.RedisCodeStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableConfigurationProperties(RedisCodeStoreProperties.class)
public class RedisCodeStoreConfiguration {

    @Bean
    public RedisCodeStore redisCodeStore(@Autowired RedisCodeStoreProperties properties,
                                         @Autowired RedisConnectionFactory factory) {
        return new RedisCodeStore(factory, properties.getKeyPrefix());
    }
}
