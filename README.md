# spring-cloud-aws-secrets-manager
property values can be injected directly into your beans from aws secrets manager.

## How to use
### gradle
```
repositories {
  jcenter()
}

dependencies {
  compile 'com.github.siosio:spring-cloud-aws-secrets-manager:2.0.0'
}
```

### disabling
please add the following to `resources/bootstrap.properties`.

```properties
aws.secretsmanager.enabled=false
```

## example
**secret name: secret-name**  

| secret key | secret value |
|------------|--------------|
| key1       | value1       |
| key2       | value2       |
| key3       | value3       |

```java
@Component
public class Bean {
    
    @Value("secret-name.key1")
    private String key1;        // injected: value1
    
    @Value("secret-name.key2")
    private String key2;        // injected: value2
    
    @Value("secret-name.key3")
    private String key3;        // injected: value3
}
```

