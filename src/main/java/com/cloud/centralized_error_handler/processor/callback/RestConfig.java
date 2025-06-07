package com.cloud.centralized_error_handler.processor.callback;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Bean
    @Profile("error-endpoint")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
