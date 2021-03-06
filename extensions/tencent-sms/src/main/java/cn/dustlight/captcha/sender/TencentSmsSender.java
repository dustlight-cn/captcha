package cn.dustlight.captcha.sender;

import cn.dustlight.captcha.TencentSmsProperties;
import cn.dustlight.captcha.core.Code;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class TencentSmsSender implements CodeSender<String> {

    private final TencentSmsProperties properties;
    private final SmsClient client;

    public TencentSmsSender(TencentSmsProperties properties) {
        this.properties = properties;

        HttpProfile profile = new HttpProfile();
        if (properties.getHttpTimeout() != null)
            profile.setConnTimeout(properties.getHttpTimeout());
        if (properties.getHttpEndpoint() != null)
            profile.setEndpoint(properties.getHttpEndpoint());
        if (properties.getHttpProxyHost() != null)
            profile.setProxyHost(properties.getHttpProxyHost());
        if (properties.getHttpProxyPort() != null)
            profile.setProxyPort(properties.getHttpProxyPort());
        if (properties.getHttpProxyUsername() != null)
            profile.setProxyUsername(properties.getHttpProxyUsername());
        if (properties.getHttpProxyPassword() != null)
            profile.setProxyPassword(properties.getHttpProxyPassword());
        if (properties.getHttpMethod() != null)
            profile.setReqMethod(properties.getHttpMethod());

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(profile);
        if (properties.getSignMethod() != null)
            clientProfile.setSignMethod(properties.getSignMethod());

        if (properties.getSecretId() == null || properties.getSecretKey() == null)
            throw new SendCodeException("Secret id or secret key can not be null! (Tencent SMS)");
        this.client = new SmsClient(new Credential(properties.getSecretId(), properties.getSecretKey()),
                properties.getRegion(), clientProfile);
    }

    @Override
    public void send(Code<String> code, Map<String, Object> parameters) throws SendCodeException {
        try {
            SendSmsRequest request = new SendSmsRequest();
            request.setSmsSdkAppid(getAppId(parameters));
            request.setSign(getSign(parameters));
            request.setSenderId(getSenderId(parameters));
            request.setTemplateID(getTemplateId(parameters));

            String phoneParamName = getPhoneParamName();

            if (!parameters.containsKey(phoneParamName) || parameters.get(phoneParamName) == null)
                throw new SendCodeException(String.format("Parameter '%s' not found!", phoneParamName));

            request.setPhoneNumberSet(new String[]{parameters.get(phoneParamName).toString()});
            String[] paramSet = getParamSet(parameters);
            if (paramSet == null)
                paramSet = new String[]{code.getValue()};
            request.setTemplateParamSet(paramSet);

            SendSmsResponse rsp = this.client.SendSms(request);
            SendStatus[] statusSet = rsp.getSendStatusSet();
            Set<SendStatus> errorSet = null;
            if (statusSet != null) {
                for (SendStatus status : statusSet) {
                    if (!status.getCode().equals("Ok")) {
                        if (errorSet == null)
                            errorSet = new LinkedHashSet<>();
                        errorSet.add(status);
                    }
                }
                if (errorSet != null && errorSet.size() > 0)
                    throw new SendCodeException("Fail to send email code. (Tencent SMS) " + this.client.gson.toJson(errorSet));
            }
        } catch (Exception e) {
            if (e instanceof SendCodeException)
                throw (SendCodeException) e;
            throw new SendCodeException("Fail to send email code. (Tencent SMS)", e);
        }
    }

    protected String[] getParamSet(Map<String, Object> parameters) {
        if (!parameters.containsKey("PARAMS"))
            return null;
        String[] paramName = parameters.get("PARAMS").toString().split(",");
        String[] values = new String[paramName.length];
        for (int i = 0; i < paramName.length; i++) {
            if (!parameters.containsKey(paramName[i]) || parameters.get(paramName[i]) == null)
                continue;
            values[i] = parameters.get(paramName[i]).toString();
        }
        return values;
    }

    protected String getAppId(Map<String, Object> parameters) {
        String key = (String) parameters.getOrDefault("APP_ID", properties.getDefaultAppId());
        return properties.getAppIds().getOrDefault(key, key);
    }

    protected String getSign(Map<String, Object> parameters) {
        String key = (String) parameters.getOrDefault("SIGN", properties.getDefaultSign());
        return properties.getSigns().getOrDefault(key, key);
    }

    protected String getSenderId(Map<String, Object> parameters) {
        String key = (String) parameters.getOrDefault("SENDER_ID", properties.getDefaultSenderId());
        return properties.getSenderIds().getOrDefault(key, key);
    }

    protected String getTemplateId(Map<String, Object> parameters) {
        String key = (String) parameters.getOrDefault("TEMPLATE_ID", properties.getDefaultTemplateId());
        return properties.getTemplateIds().getOrDefault(key, key);
    }

    protected String getPhoneParamName() {
        return StringUtils.hasText(properties.getPhoneParamName()) ? properties.getPhoneParamName() : "phone";
    }
}
