package com.murilo.market_place.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
public class AwsS3Config {

    @Value("${aws.keys.access_key}")
    private String accessKey;

    @Value("${aws.keys.secret_key}")
    private String secretKey;

    @Bean
    public S3Client createS3Instance() {
        S3ClientBuilder s3ClientBuilder = S3Client.builder().region(Region.US_EAST_1);

        if (!accessKey.isBlank() && !secretKey.isBlank()) {
            s3ClientBuilder.credentialsProvider(
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
            );
        }

        return s3ClientBuilder.build();
    }
}
