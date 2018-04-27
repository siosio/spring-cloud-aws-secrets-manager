package com.github.siosio

import com.amazonaws.auth.*
import com.amazonaws.services.secretsmanager.*
import com.amazonaws.services.secretsmanager.model.*
import com.fasterxml.jackson.databind.*
import org.springframework.boot.*
import org.springframework.boot.env.*
import org.springframework.core.env.*

class AwsSecretsManagerEnvironmentPostProcessor : EnvironmentPostProcessor {

    companion object {
        private val objectMapper = ObjectMapper()
    }

    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {

        if (environment.getProperty("aws.secretsmanager.enabled", Boolean::class.java, true).not()) {
            return
        }
        
        val region = environment.getProperty("cloud.aws.region.static")

        val client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build()

        val propertySources = environment.propertySources
        client.listSecrets(ListSecretsRequest()).secretList
                .map { Pair(it.name, GetSecretValueRequest().withSecretId(it.name)) }
                .map { Pair(it.first, client.getSecretValue(it.second)) }
                .map { Pair(it.first, it.second.getSecretString()) }
                .map {
                    @Suppress("UNCHECKED_CAST")
                    Pair(it.first, objectMapper.readValue(it.second, Map::class.java) as Map<String, Any>)
                }
                .forEach { propertySources.addFirst(MapPropertySource(it.first, it.second)) }
    }
}