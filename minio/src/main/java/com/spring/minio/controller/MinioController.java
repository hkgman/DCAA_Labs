package com.spring.minio.controller;

import com.spring.minio.config.MinioAdapter;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@Slf4j
public class MinioController {
    private final MinioAdapter minioAdapter;
    public static final String CORRELATION_ID = "correlation-id";

    public MinioController(MinioAdapter minioAdapter) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        this.minioAdapter = minioAdapter;
        if (minioAdapter.getBuckets().stream().map(Bucket::name).findAny().isEmpty()) {
            minioAdapter.createBucket("user1");
        }
    }

    @GetMapping("/buckets")
    public ResponseEntity<List<String>> get(@RequestHeader(CORRELATION_ID) String correlationId) {
        log.info(String.format("correlation-id: %s /buckets", correlationId));
        return new ResponseEntity<>(minioAdapter.getBuckets().stream().map(Bucket::name).toList(), setHeaders(correlationId), HttpStatus.OK);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> post(@RequestPart(value = "file", required = false) MultipartFile file, @RequestHeader(CORRELATION_ID) String correlationId) throws IOException {
        log.info(String.format("correlation-id: %s /upload", correlationId));
        if (minioAdapter.uploadFile("user1", file.getOriginalFilename(), file.getBytes())) {
            return new ResponseEntity<>(file.getOriginalFilename(), setHeaders(correlationId), HttpStatus.OK);
        } else return new ResponseEntity<>("File is already exists", setHeaders(correlationId), HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<?> get(@RequestPart("file") String file, @RequestHeader(CORRELATION_ID) String correlationId) {
        log.info(String.format("correlation-id: %s /download", correlationId));
        try {
            byte[] data = minioAdapter.getFile("user1", file);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-Type", "application/octet-stream")
                    .header("Content-Disposition", "attachment; filename=\"" + file + "\"")
                    .header(correlationId)
                    .body(resource);
        } catch (Exception e) {
            return new ResponseEntity<>("No file", setHeaders(correlationId), HttpStatus.NOT_FOUND);
        }
    }

    private HttpHeaders setHeaders(String info) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CORRELATION_ID, info);
        return headers;
    }
}
