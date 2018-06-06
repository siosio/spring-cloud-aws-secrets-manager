package com.github.siosio;

import java.io.IOException;
import java.util.Map;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.ListSecretsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

public class AwsSecretsManagerEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {
        if (!environment.getProperty("aws.secretsmanager.enabled", Boolean.class, true)) {
            return;
        }
        final String region = environment.getProperty("cloud.aws.region.static");

        final AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                                                                       .withRegion(region)
                                                                       .build();

        final MutablePropertySources propertySources = environment.getPropertySources();

        client.listSecrets(new ListSecretsRequest())
              .getSecretList()
              .forEach(entry ->
                      {
                          final String name = entry.getName();
                          final GetSecretValueResult result = client.getSecretValue(
                                  new GetSecretValueRequest().withSecretId(name));
                          propertySources.addFirst(new MapPropertySource(name, jsonToMap(result.getSecretString())));
                      }
              );
    }

    private Map<String, Object> jsonToMap(String jsonString) {
        try {
            //noinspection unchecked
            return OBJECT_MAPPER.readValue(jsonString, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}