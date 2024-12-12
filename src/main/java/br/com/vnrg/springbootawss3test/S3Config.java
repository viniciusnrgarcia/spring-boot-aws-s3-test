package br.com.vnrg.springbootawss3test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {


        return S3Client.builder()
                .region(Region.SA_EAST_1)
                .endpointOverride(URI.create("http://localhost:4566"))
                //.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // Path-style access for LocalStack
                        .build())
                .build();

//        return S3Client.builder()
//                .region(Region.SA_EAST_1)
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create("access-key", "secret-key")))
//                .endpointOverride(URI.create("http://localhost:4566"))
//                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
//                .build();
    }
}
