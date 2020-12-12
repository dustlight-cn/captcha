package cn.dustlight.captcha.core;

import cn.dustlight.captcha.configurations.DefaultBeanProperties;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

public class SendCodePostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    public SendCodePostProcessor(DefaultBeanProperties defaultBeanProperties) {
        this.advisor = new SendCodeAdvisor(defaultBeanProperties);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        ((SendCodeAdvisor) this.advisor).setFactory(beanFactory);
    }

    @Override
    public int getOrder() {
        return ((SendCodeAdvisor) this.advisor).getOrder();
    }

    @Override
    public void setOrder(int order) {
        ((SendCodeAdvisor) this.advisor).setOrder(order);
    }
}
