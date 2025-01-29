package com.codeit.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final S3Client s3Client;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final String DIR = "codeit";

    public String upload(MultipartFile file) throws IOException {
        // 임시 파일 생성
        Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), file.getOriginalFilename());
        file.transferTo(tempFile.toFile());

        // S3에 파일 업로드
        String fileName = DIR + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, tempFile);

        // S3 파일 URL 반환
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, "ap-northeast-2", fileName);
    }
}
