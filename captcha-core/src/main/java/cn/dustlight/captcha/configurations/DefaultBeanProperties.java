package cn.dustlight.captcha.configurations;

import cn.dustlight.captcha.generator.CodeGenerator;
import cn.dustlight.captcha.sender.CodeSender;
import cn.dustlight.captcha.store.CodeStore;
import cn.dustlight.captcha.verifier.CodeVerifier;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.captcha.default")
public class DefaultBeanProperties {

    private Item<CodeGenerator> generator = new Item("randomStringCodeGenerator", CodeGenerator.class);
    private Item<CodeStore> store = new Item("httpSessionCodeStore", CodeStore.class);
    private Item<CodeSender> sender = new Item("simpleImageCodeSender", CodeSender.class);
    private Item<CodeVerifier> verifier = new Item("stringCodeVerifier", CodeVerifier.class);

    public Item<CodeGenerator> getGenerator() {
        return generator;
    }

    public void setGenerator(Item<CodeGenerator> generator) {
        this.generator = generator;
    }

    public Item<CodeStore> getStore() {
        return store;
    }

    public void setStore(Item<CodeStore> store) {
        this.store = store;
    }

    public Item<CodeSender> getSender() {
        return sender;
    }

    public void setSender(Item<CodeSender> sender) {
        this.sender = sender;
    }

    public Item<CodeVerifier> getVerifier() {
        return verifier;
    }

    public void setVerifier(Item<CodeVerifier> verifier) {
        this.verifier = verifier;
    }

    public static class Item<T> {

        private String name;
        private Class<T> type;

        public Item() {
            this(null, null);
        }

        public Item(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<T> getType() {
            return type;
        }

        public void setType(Class<T> type) {
            this.type = type;
        }
    }
}
