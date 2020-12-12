package cn.dustlight.captcha.configurations;

import cn.dustlight.captcha.store.HttpSessionCodeStore;
import org.springframework.context.annotation.Bean;

public class HttpSessionCodeStoreConfiguration {

    @Bean
    public HttpSessionCodeStore httpSessionCodeStore() {
        HttpSessionCodeStore store = new HttpSessionCodeStore();
        return store;
    }
}
