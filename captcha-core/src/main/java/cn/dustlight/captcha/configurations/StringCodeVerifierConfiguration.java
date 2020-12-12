package cn.dustlight.captcha.configurations;

import cn.dustlight.captcha.verifier.StringCodeVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(StringCodeVerifierConfiguration.StringCodeVerifierProperties.class)
public class StringCodeVerifierConfiguration {

    @Bean
    public StringCodeVerifier stringCodeVerifier(@Autowired StringCodeVerifierProperties properties) {
        StringCodeVerifier verifier = new StringCodeVerifier();
        if (properties != null)
            verifier.setHandler(new StringCodeVerifier.StringEqualsVerifierHandler(properties.isAllowNulls(),
                    properties.isTrim(),
                    properties.isCaseSensitive()));
        return verifier;
    }

    @ConfigurationProperties("dustlight.captcha.verifier.string-equals")
    public static class StringCodeVerifierProperties {

        private boolean allowNulls = false;
        private boolean trim = false;
        private boolean caseSensitive = false;

        public boolean isAllowNulls() {
            return allowNulls;
        }

        public void setAllowNulls(boolean allowNulls) {
            this.allowNulls = allowNulls;
        }

        public boolean isTrim() {
            return trim;
        }

        public void setTrim(boolean trim) {
            this.trim = trim;
        }

        public boolean isCaseSensitive() {
            return caseSensitive;
        }

        public void setCaseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }
    }
}
