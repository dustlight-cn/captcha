package cn.dustlight.captcha.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class ReCaptchaResult implements Serializable {

    private Boolean success;

    private String hostname;

    @JsonProperty("apk_package_name")
    private String apkPackageName;

    @JsonProperty("challenge_ts")
    private Date challengeTimestamp;

    @JsonProperty("error-codes")
    private Collection<String> errorCodes;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public Date getChallengeTimestamp() {
        return challengeTimestamp;
    }

    public void setChallengeTimestamp(Date challengeTimestamp) {
        this.challengeTimestamp = challengeTimestamp;
    }

    public Collection<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Collection<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "ReCaptchaResult{" +
                "success=" + success +
                ", hostname='" + hostname + '\'' +
                ", apkPackageName='" + apkPackageName + '\'' +
                ", challengeTimestamp=" + challengeTimestamp +
                ", errorCodes=" + errorCodes +
                '}';
    }
}
