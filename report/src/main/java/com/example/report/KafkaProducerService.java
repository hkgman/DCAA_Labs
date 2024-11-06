package com.example.report;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "name";

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String key,byte[] message) {
        kafkaTemplate.send(TOPIC,key, message);
    }
}
