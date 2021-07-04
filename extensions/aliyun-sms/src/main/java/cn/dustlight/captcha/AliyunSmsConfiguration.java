package cn.dustlight.captcha;

import cn.dustlight.captcha.sender.AliyunSmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(AliyunSmsProperties.class)
public class AliyunSmsConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "dustlight.captcha.sender.alibaba.sms",
            value = {"access-key-id", "access-key-secret"})
    public AliyunSmsSender aliyunSmsSender(@Autowired AliyunSmsProperties properties) throws Exception {
        return new AliyunSmsSender(properties);
    }
}
