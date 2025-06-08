package com.cloud.error_handler.processor.callback;

import com.cloud.error_handler.ErrorProcessorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import response.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Profile("error-endpoint")
public class CallbackProcessorService implements ErrorProcessorService {

    private final RestTemplate restTemplate;
    private final String endpointUrl;
    private static final Logger logger = LoggerFactory.getLogger(CallbackProcessorService.class);

    public CallbackProcessorService(RestTemplate restTemplate,
                                    @Value("${error.rest.endpoint.url:http://localhost:8081/errors}") String endpointUrl) {
        this.restTemplate = restTemplate;
        this.endpointUrl = endpointUrl;
    }

    @Override
    public void process(ApiError apiError) {
        try {
            restTemplate.postForEntity(endpointUrl, apiError, Void.class);
            logger.info("Error successfully sent to endpoint: {}", endpointUrl);
        } catch (Exception e) {
            logger.error("Failed to send to endpoint {} the error:", endpointUrl, e);
        }
    }
}
