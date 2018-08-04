package com.github.siosio;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.ListSecretsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

public class AwsSecretsManagerPropertySourceLocator implements PropertySourceLocator {

    private final AWSSecretsManager client;

    public AwsSecretsManagerPropertySourceLocator(
            final AWSSecretsManager client, final AwsSecretsManagerProperties properties) {
        this.client = client;
    }

    @Override
    public PropertySource<?> locate(final Environment environment) {
        final CompositePropertySource propertySource = new CompositePropertySource("aws-secrets-store");
        client.listSecrets(new ListSecretsRequest())
              .getSecretList()
              .forEach(entry -> {
                  final String secretName = entry.getName();
                  final GetSecretValueResult result = client.getSecretValue(
                          new GetSecretValueRequest().withSecretId(secretName));
                  propertySource.addPropertySource(
                          new MapPropertySource(secretName, jsonToMap(secretName, result.getSecretString())));
              });
        return propertySource;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Map<String, Object> jsonToMap(final String name, String jsonString) {
        try {
            //noinspection unchecked
            final Map<String, Object> map = OBJECT_MAPPER.readValue(jsonString, Map.class);
            return map.entrySet()
               .stream()
               .collect(
                       Collectors.toMap(
                               e -> name.replaceAll("/", ".") + '.' + e.getKey(),
                               Entry::getValue
                       ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
