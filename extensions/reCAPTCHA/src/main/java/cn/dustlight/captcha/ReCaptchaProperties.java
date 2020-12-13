package cn.dustlight.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.captcha.recaptcha")
public class ReCaptchaProperties {

    private String endpoint = "https://www.recaptcha.net/recaptcha/api/siteverify";
    private String defaultSecret;
    private String defaultParameterName = "g-recaptcha-response";

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDefaultSecret() {
        return defaultSecret;
    }

    public void setDefaultSecret(String defaultSecret) {
        this.defaultSecret = defaultSecret;
    }

    public String getDefaultParameterName() {
        return defaultParameterName;
    }

    public void setDefaultParameterName(String defaultParameterName) {
        this.defaultParameterName = defaultParameterName;
    }
}
