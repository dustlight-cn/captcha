package cn.dustlight.captcha;

import cn.dustlight.captcha.sender.TencentSmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(TencentSmsProperties.class)
public class TencentSmsConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "dustlight.captcha.sender.tencent.sms",
            value = {"secret-id", "secret-key"})
    public TencentSmsSender tencentSmsSender(@Autowired TencentSmsProperties properties) {
        return new TencentSmsSender(properties);
    }
}
