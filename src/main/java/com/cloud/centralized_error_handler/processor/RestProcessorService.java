package com.cloud.centralized_error_handler.processor;

import com.cloud.centralized_error_handler.ErrorProcessorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import response.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Primary
public class RestProcessorService implements ErrorProcessorService {

    private final RestTemplate restTemplate;
    private final String endpointUrl;
    private static final Logger logger = LoggerFactory.getLogger(RestProcessorService.class);

    public RestProcessorService(RestTemplate restTemplate, @Value("${error.rest.endpoint.url:http://localhost:8081/errors}") String endpointUrl) {
        this.restTemplate = restTemplate;
        this.endpointUrl = endpointUrl;
    }

    @Override
    public void process(ApiError apiError) {
        try {
            restTemplate.postForEntity(endpointUrl, apiError, Void.class);
        } catch (Exception e) {
            logger.error("Error at send the request", e);
        }
    }
}
