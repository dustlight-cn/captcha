package cn.dustlight.captcha;

import cn.dustlight.captcha.store.ReCaptchaStore;
import cn.dustlight.captcha.store.ReCaptchaV3Store;
import cn.dustlight.captcha.verifier.ReCaptchaVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(ReCaptchaProperties.class)
public class ReCaptchaConfiguration {

    @Bean
    public ReCaptchaStore reCaptchaStore(@Autowired ReCaptchaProperties properties) {
        return new ReCaptchaStore(properties);
    }

    @Bean
    public ReCaptchaV3Store reCaptchaV3Store(@Autowired ReCaptchaProperties properties) {
        return new ReCaptchaV3Store(properties);
    }

    @Bean
    public ReCaptchaVerifier reCaptchaVerifier() {
        return new ReCaptchaVerifier();
    }
}
