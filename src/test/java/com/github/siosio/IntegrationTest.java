package com.github.siosio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IntegrationTest.Comp.class, IntegrationTest.Properties.class})
public class IntegrationTest {
    
    @Autowired
    private Comp comp;
    
    @Autowired
    private Properties properties; 

    @Test
    public void injectFromAWSSecretsManager() {
        System.out.println("******************************");
        System.out.println("comp.testValue = " + comp.testValue);
        System.out.println("properties.getId() = " + properties.getId());
        System.out.println("******************************");
    }
    
    @SpringBootApplication
    @Component
    public static class Comp {
        @Value("${prod.test-data.test.id}")
        private String testValue;
    }
    
    @Component
    @ConfigurationProperties(prefix = "prod.test-data.test")
    public static class Properties {
        
        private String id;

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }
    }

}
