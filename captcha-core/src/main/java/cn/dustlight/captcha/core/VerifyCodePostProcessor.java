package cn.dustlight.captcha.core;

import cn.dustlight.captcha.configurations.DefaultBeanProperties;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

public class VerifyCodePostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    public VerifyCodePostProcessor(DefaultBeanProperties defaultBeanProperties) {
        this.advisor = new VerifyCodeAdvisor(defaultBeanProperties);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        ((VerifyCodeAdvisor) this.advisor).setFactory(beanFactory);
    }

    @Override
    public int getOrder() {
        return ((VerifyCodeAdvisor) this.advisor).getOrder();
    }

    @Override
    public void setOrder(int order) {
        ((VerifyCodeAdvisor) this.advisor).setOrder(order);
    }
}
