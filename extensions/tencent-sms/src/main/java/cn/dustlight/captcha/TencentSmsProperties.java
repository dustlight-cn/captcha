package cn.dustlight.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("dustlight.captcha.sender.tencent.sms")
public class TencentSmsProperties {

    private String secretId, secretKey, region, defaultAppId, defaultSign, defaultSenderId, defaultTemplateId;

    private String httpProxyHost, httpProxyUsername, httpProxyPassword,
            httpMethod = "POST",
            httpEndpoint,
            signMethod = "TC3-HMAC-SHA256";

    private String phoneParamName = "phone";

    private Integer httpProxyPort, httpTimeout = 60;

    private Map<String,String> appIds = new HashMap<>();
    private Map<String,String> signs = new HashMap<>();
    private Map<String,String> senderIds = new HashMap<>();
    private Map<String,String> templateIds = new HashMap<>();

    public Map<String, String> getAppIds() {
        return appIds;
    }

    public void setAppIds(Map<String, String> appIds) {
        this.appIds = appIds;
    }

    public Map<String, String> getSigns() {
        return signs;
    }

    public void setSigns(Map<String, String> signs) {
        this.signs = signs;
    }

    public Map<String, String> getSenderIds() {
        return senderIds;
    }

    public void setSenderIds(Map<String, String> senderIds) {
        this.senderIds = senderIds;
    }

    public Map<String, String> getTemplateIds() {
        return templateIds;
    }

    public void setTemplateIds(Map<String, String> templateIds) {
        this.templateIds = templateIds;
    }

    public String getPhoneParamName() {
        return phoneParamName;
    }

    public void setPhoneParamName(String phoneParamName) {
        this.phoneParamName = phoneParamName;
    }

    public String getHttpProxyUsername() {
        return httpProxyUsername;
    }

    public void setHttpProxyUsername(String httpProxyUsername) {
        this.httpProxyUsername = httpProxyUsername;
    }

    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    public Integer getHttpTimeout() {
        return httpTimeout;
    }

    public void setHttpTimeout(Integer httpTimeout) {
        this.httpTimeout = httpTimeout;
    }

    public Integer getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(Integer httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDefaultAppId() {
        return defaultAppId;
    }

    public void setDefaultAppId(String defaultAppId) {
        this.defaultAppId = defaultAppId;
    }

    public String getDefaultSign() {
        return defaultSign;
    }

    public void setDefaultSign(String defaultSign) {
        this.defaultSign = defaultSign;
    }

    public String getDefaultSenderId() {
        return defaultSenderId;
    }

    public void setDefaultSenderId(String defaultSenderId) {
        this.defaultSenderId = defaultSenderId;
    }

    public String getDefaultTemplateId() {
        return defaultTemplateId;
    }

    public void setDefaultTemplateId(String defaultTemplateId) {
        this.defaultTemplateId = defaultTemplateId;
    }

    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpEndpoint() {
        return httpEndpoint;
    }

    public void setHttpEndpoint(String httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }
}
