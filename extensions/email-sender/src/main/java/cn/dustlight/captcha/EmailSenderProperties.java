package cn.dustlight.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.captcha.sender.email")
public class EmailSenderProperties {

    private String emailParamName = "email";
    private String defaultSubject = "Captcha";
    private boolean html = true;

    public String getDefaultSubject() {
        return defaultSubject;
    }

    public void setDefaultSubject(String defaultSubject) {
        this.defaultSubject = defaultSubject;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public String getEmailParamName() {
        return emailParamName;
    }

    public void setEmailParamName(String emailParamName) {
        this.emailParamName = emailParamName;
    }
}
