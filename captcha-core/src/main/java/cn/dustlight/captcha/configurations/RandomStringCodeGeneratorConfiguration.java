package cn.dustlight.captcha.configurations;

import cn.dustlight.captcha.generator.RandomStringCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(RandomStringCodeGeneratorConfiguration.RandomStringCodeGeneratorProperties.class)
public class RandomStringCodeGeneratorConfiguration {

    @Bean
    public RandomStringCodeGenerator randomStringCodeGenerator(@Autowired RandomStringCodeGeneratorProperties properties) {
        RandomStringCodeGenerator generator = new RandomStringCodeGenerator();
        if (properties != null) {
            generator.setChars(properties.getChars());
            generator.setLength(properties.getLength());
        }
        return generator;
    }

    @ConfigurationProperties("dustlight.captcha.generator.random-string")
    public static class RandomStringCodeGeneratorProperties {

        private char[] chars = RandomStringCodeGenerator.DEFAULT_CHARACTERS;
        private int length = 4;

        public char[] getChars() {
            return chars;
        }

        public void setChars(char[] chars) {
            this.chars = chars;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }
}
