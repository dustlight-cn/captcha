package cn.dustlight.captcha.sender;

import cn.dustlight.captcha.AliyunSmsProperties;
import cn.dustlight.captcha.core.Code;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.google.gson.Gson;
import org.apache.catalina.util.ParameterMap;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class AliyunSmsSender implements CodeSender<String> {

    private final AliyunSmsProperties properties;
    private final Client client;
    private Gson gson;

    public AliyunSmsSender(AliyunSmsProperties properties) throws Exception {
        if (properties.getAccessKeyId() == null || properties.getAccessKeySecret() == null)
            throw new SendCodeException("Access Key ID or Access Key Secret can not be null! (Aliyun SMS)");

        this.properties = properties;
        Config config = new Config();
        if(properties.getProtocol() != null)
            config.setProtocol(properties.getProtocol());
        if(properties.getEndpoint() != null)
            config.setEndpoint(properties.getEndpoint());

        if(properties.getSignatureAlgorithm() != null)
            config.setSignatureAlgorithm(properties.getSignatureAlgorithm());

        if(properties.getHttpTimeout() != null) {
            config.setConnectTimeout(properties.getHttpTimeout());
            config.setReadTimeout(properties.getHttpTimeout());
        }

        config.setAccessKeyId(properties.getAccessKeyId());
        config.setAccessKeySecret(properties.getAccessKeySecret());

        this.client = new Client(config);
        gson = new Gson();
    }

    @Override
    public void send(Code<String> code, Map<String, Object> parameters) throws SendCodeException {
        try {
            SendSmsRequest request = new SendSmsRequest();
            request.setSignName(getSignName(parameters));
            request.setTemplateCode(getTemplateCode(parameters));

            String phoneParamName = getPhoneParamName();

            if (!parameters.containsKey(phoneParamName) || parameters.get(phoneParamName) == null)
                throw new SendCodeException(String.format("Parameter '%s' not found!", phoneParamName));
            request.setPhoneNumbers(parameters.get(phoneParamName).toString());

            Map<String,Object> paramMap = getParamMap(parameters);
            if (paramMap == null)
            {
                paramMap = new ParameterMap<>();
                paramMap.put("code",code.getValue());
            }
            request.setTemplateParam(gson.toJson(paramMap));

            SendSmsResponse rsp = this.client.sendSms(request);

            if(!"OK".equals(rsp.getBody().getCode())){
                throw new SendCodeException("Fail to send email code. (Aliyun SMS) " + this.gson.toJson(rsp.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SendCodeException)
                throw (SendCodeException) e;
            throw new SendCodeException("Fail to send email code. (Aliyun SMS)", e);
        }
    }

    protected Map<String,Object> getParamMap(Map<String, Object> parameters) {
        if (!parameters.containsKey("PARAMS"))
            return null;
        String[] paramName = parameters.get("PARAMS").toString().split(",");
        Map<String,Object> result = new LinkedHashMap<>();
        for (int i = 0; i < paramName.length; i++) {
            if (!parameters.containsKey(paramName[i]) || parameters.get(paramName[i]) == null)
                continue;
            result.put(paramName[i],parameters.get(paramName[i]));
        }
        return result;
    }

    protected String getSignName(Map<String, Object> parameters) {
        return (String) parameters.getOrDefault("SIGN_NAME", properties.getDefaultSignName());
    }

    protected String getTemplateCode(Map<String, Object> parameters) {
        return (String) parameters.getOrDefault("TEMPLATE_CODE", properties.getDefaultTemplateCode());
    }

    protected String getPhoneParamName() {
        return StringUtils.hasText(properties.getPhoneParamName()) ? properties.getPhoneParamName() : "phone";
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
