package cn.dustlight.captcha.configurations;

import cn.dustlight.captcha.sender.SimpleImageCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(SimpleImageCodeSenderConfiguration.SimpleImageCodeSenderProperties.class)
public class SimpleImageCodeSenderConfiguration {

    @Bean
    public SimpleImageCodeSender simpleImageCodeSender(@Autowired SimpleImageCodeSenderProperties properties) {
        SimpleImageCodeSender sender = new SimpleImageCodeSender();
        if (properties != null) {
            sender.setWidth(properties.getWidth());
            sender.setHeight(properties.getHeight());
            sender.setImageHandler(new SimpleImageCodeSender.DefaultImageHandler(properties.getFonts()));
        }
        return sender;
    }

    @ConfigurationProperties("dustlight.captcha.sender.simple-image")
    public static class SimpleImageCodeSenderProperties {

        private int width = 200, height = 100;
        private String[] fonts = new String[]{"Georgia"};

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String[] getFonts() {
            return fonts;
        }

        public void setFonts(String[] fonts) {
            this.fonts = fonts;
        }
    }
}
