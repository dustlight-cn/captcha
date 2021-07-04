package cn.dustlight.captcha;

import cn.dustlight.captcha.sender.EmailCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@EnableConfigurationProperties(EmailSenderProperties.class)
public class EmailSenderConfiguration {

    @Bean("templateProvider")
    @ConditionalOnMissingBean(value = EmailCodeSender.TemplateProvider.class)
    public EmailCodeSender.TemplateProvider templateProvider() {
        return new EmailCodeSender.DefaultTemplateProvider();
    }

    @Bean
    @ConditionalOnBean(value = {JavaMailSender.class})
    public EmailCodeSender emailCodeSender(@Autowired EmailSenderProperties properties,
                                           @Autowired MailProperties mailProperties,
                                           @Autowired JavaMailSender javaMailSender,
                                           @Autowired EmailCodeSender.TemplateProvider templateProvider) {
        return new EmailCodeSender(javaMailSender, mailProperties, properties, templateProvider);
    }

}
