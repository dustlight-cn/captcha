package cn.dustlight.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.captcha.store.redis")
public class RedisCodeStoreProperties {

    private String keyPrefix = "CAPTCHA_CODE";

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
