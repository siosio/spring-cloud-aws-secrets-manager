package com.github.siosio;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author siosio
 */
@Configuration
@EnableConfigurationProperties(AwsSecretsManagerProperties.class)
@ConditionalOnClass(AWSSecretsManager.class)
@ConditionalOnProperty(value = "aws.secretsmanager.enabled", matchIfMissing = true, havingValue = "true")
public class AwsSecretsManagerBootstrapConfiguration {
    
    private final AwsSecretsManagerProperties properties;

    public AwsSecretsManagerBootstrapConfiguration(final AwsSecretsManagerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public AwsSecretsManagerPropertySourceLocator awsSecretsManager(
            @Value("${cloud.aws.region.static:}") final String region) {
        final AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                                                                       .withRegion(region)
                                                                       .build();
        return new AwsSecretsManagerPropertySourceLocator(client, properties);
    }
}
