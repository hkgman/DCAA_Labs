package com.spring.minio;

import com.spring.minio.config.MinioAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final MinioAdapter minioAdapter;
    @KafkaListener(topics = "name", groupId = "group_id")
    public void consume(ConsumerRecord<String,byte[]> record) {
        try
        {
            log.info("Hello its work");
            File file = new File(record.key()+".csv");
            FileUtils.writeByteArrayToFile(file, record.value());
            minioAdapter.uploadFile("user1", file.getName(), Files.readAllBytes(file.toPath()));
        }catch (Exception e)
        {
            log.error("Error: ",e);
        }

    }
}
