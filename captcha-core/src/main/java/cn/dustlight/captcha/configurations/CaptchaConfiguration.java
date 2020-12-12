package cn.dustlight.captcha.configurations;

import cn.dustlight.captcha.annotations.EnableCaptcha;
import cn.dustlight.captcha.core.SendCodePostProcessor;
import cn.dustlight.captcha.core.VerifyCodePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

@Configuration(
        proxyBeanMethods = false
)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(DefaultBeanProperties.class)
public class CaptchaConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes attrs;

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        this.attrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableCaptcha.class.getName(), false));
        if (this.attrs == null) {
            throw new IllegalArgumentException("@EnableValidator is not present on importing class " + annotationMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SendCodePostProcessor sendCodePostProcessor(@Autowired DefaultBeanProperties defaultBeanProperties) {
        SendCodePostProcessor sendCodePostProcessor = new SendCodePostProcessor(defaultBeanProperties);
        if (attrs != null) {
            sendCodePostProcessor.setProxyTargetClass(attrs.getBoolean("proxyTargetClass"));
            sendCodePostProcessor.setOrder(attrs.getNumber("orderOfSend"));
        }
        return sendCodePostProcessor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public VerifyCodePostProcessor verifyCodePostProcessor(@Autowired DefaultBeanProperties defaultBeanProperties) {
        VerifyCodePostProcessor verifyCodePostProcessor = new VerifyCodePostProcessor(defaultBeanProperties);
        if (attrs != null) {
            verifyCodePostProcessor.setProxyTargetClass(attrs.getBoolean("proxyTargetClass"));
            verifyCodePostProcessor.setOrder(attrs.getNumber("orderOfVerify"));
        }
        return verifyCodePostProcessor;
    }
}
