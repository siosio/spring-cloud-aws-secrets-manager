package com.github.siosio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTest.Comp.class)
public class IntegrationTest {
    
    @Autowired
    public Comp comp;

    @Test
    public void injectFromAWSSecretsManager() {
        System.out.println("******************************");
        System.out.println("app.testValue = " + comp.testValue);
        System.out.println("******************************");
    }
    
    @SpringBootApplication
    @Component
    public static class Comp {
        @Value("${test.id}")
        public String testValue;
    }

}
