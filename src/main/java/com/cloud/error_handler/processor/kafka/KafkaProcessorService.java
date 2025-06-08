package com.cloud.error_handler.processor.kafka;

import com.cloud.error_handler.ErrorProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import response.ApiError;

@Service
@Profile("error-kafka")
public class KafkaProcessorService implements ErrorProcessorService {

    private final KafkaTemplate<String, ApiError> kafkaTemplate;
    private final String topic;
    private static final Logger logger = LoggerFactory.getLogger(KafkaProcessorService.class);


    public KafkaProcessorService(KafkaTemplate<String, ApiError> kafkaTemplate, @Value("${error.kafka.topic:error-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void process(ApiError apiError) {
        kafkaTemplate.send(topic, apiError);
        try {
            kafkaTemplate.send(topic, apiError);
            logger.info("Error sent to Kafka topic: {}", topic);
        } catch (Exception e) {
            logger.error("Failed to send error to Kafka topic: {}", topic, e);
        }
    }
}
