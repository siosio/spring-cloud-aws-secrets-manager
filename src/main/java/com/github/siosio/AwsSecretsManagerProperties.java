package com.github.siosio;

import static com.github.siosio.AwsSecretsManagerProperties.PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = PREFIX)
public class AwsSecretsManagerProperties {

    static final String PREFIX = "aws.secretsmanager";
    
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
