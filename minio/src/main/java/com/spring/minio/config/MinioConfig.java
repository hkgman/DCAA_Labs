package com.spring.minio.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.access.name}")
    private String accessName;
    @Value("${minio.access.secret}")
    private String accessSecret;
    @Value("${minio.url}")
    private String minioUrl;

    @Bean
    public MinioClient minioClient() {
        try {
            return MinioClient.builder()
                    .credentials(accessName, accessSecret)
                    .endpoint(minioUrl)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
