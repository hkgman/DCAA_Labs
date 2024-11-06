package com.spring.minio.config;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class MinioAdapter {
    private final MinioClient minioClient;

    public MinioAdapter(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public List<Bucket> getBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        boolean isExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public Boolean uploadFile(String bucket, String fileName, byte[] content) {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName).stream(new FileInputStream(fileName), file.length(), -1)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return true;
    }

    public byte[] getFile(String bucket, String fileName) {
        try (InputStream in = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(fileName)
                .build())) {
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
