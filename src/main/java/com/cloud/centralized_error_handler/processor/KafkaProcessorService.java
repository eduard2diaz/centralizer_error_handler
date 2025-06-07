package com.cloud.centralized_error_handler.processor;

import com.cloud.centralized_error_handler.ErrorProcessorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import response.ApiError;

@Service
public class KafkaProcessorService implements ErrorProcessorService {

    private final KafkaTemplate<String, ApiError> kafkaTemplate;
    private final String topic;

    public KafkaProcessorService(KafkaTemplate<String, ApiError> kafkaTemplate, @Value("${error.kafka.topic:error-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void process(ApiError apiError) {
        kafkaTemplate.send(topic, apiError);
    }
}
