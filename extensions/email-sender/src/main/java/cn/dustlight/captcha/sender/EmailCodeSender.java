package cn.dustlight.captcha.sender;

import cn.dustlight.captcha.EmailSenderProperties;
import cn.dustlight.captcha.core.Code;
import freemarker.template.Template;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Map;

public class EmailCodeSender implements CodeSender<String> {

    private final EmailSenderProperties properties;
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    private TemplateProvider templateProvider;

    public EmailCodeSender(JavaMailSender javaMailSender,
                           MailProperties mailProperties,
                           EmailSenderProperties properties,
                           TemplateProvider templateProvider) {

        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
        this.properties = properties;
        this.templateProvider = templateProvider;

    }

    @Override
    public void send(Code<String> code, Map<String, Object> parameters) throws SendCodeException {
        try {
            String template = getTemplate(parameters);
            if (template == null)
                throw new SendCodeException("Email template not found!");
            String emailParamName = getEmailParam();
            if (!parameters.containsKey(emailParamName) || parameters.get(emailParamName) == null)
                throw new SendCodeException(String.format("Parameter '%s' not found!", emailParamName));
            String email = parameters.get(emailParamName).toString();

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(message);
            h.setFrom(getFrom(parameters));
            h.setTo(email);
            h.setText(template, isHtml(parameters));
            h.setSubject(getSubject(parameters));
            javaMailSender.send(message);
        } catch (Exception e) {
            if (e instanceof SendCodeException)
                throw (SendCodeException) e;
            throw new SendCodeException("Send Email code error!", e);
        }
    }

    public TemplateProvider getTemplateProvider() {
        return templateProvider;
    }

    public void setTemplateProvider(TemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
    }

    protected String getEmailParam() {
        return StringUtils.hasText(properties.getEmailParamName()) ? properties.getEmailParamName() : "email";
    }

    protected String getTemplate(Map<String, Object> parameters) throws Exception {
        if (!parameters.containsKey("TEMPLATE"))
            return null;
        return templateProvider.getTemplateContent((String) parameters.get("TEMPLATE"), parameters);
    }

    protected boolean isHtml(Map<String, Object> parameters) {
        return (boolean) parameters.getOrDefault("HTML", properties.isHtml());
    }

    protected String getSubject(Map<String, Object> parameters) {
        return (String) parameters.getOrDefault("SUBJECT", properties.getDefaultSubject());
    }

    protected String getFrom(Map<String, Object> parameters) {
        return (String) parameters.getOrDefault("FROM", mailProperties.getUsername());
    }

    public interface TemplateProvider {
        String getTemplateContent(String templateName, Map<String, Object> parameters) throws Exception;
    }

    public static class DefaultTemplateProvider implements TemplateProvider {

        @Override
        public String getTemplateContent(String templateName, Map<String, Object> parameters) throws Exception {
            try (Reader reader = getReader(templateName)) {
                return FreeMarkerTemplateUtils.processTemplateIntoString(
                        new Template(templateName, reader, null),
                        parameters);
            }
        }

        protected Reader getReader(String templateName) throws IOException {
            return new BufferedReader(new InputStreamReader(new ClassPathResource(templateName).getInputStream()));
        }
    }
}
