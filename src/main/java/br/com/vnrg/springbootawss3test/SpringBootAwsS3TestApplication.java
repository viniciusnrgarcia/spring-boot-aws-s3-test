package br.com.vnrg.springbootawss3test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

@SpringBootApplication
public class SpringBootAwsS3TestApplication {

	public static void main(String[] args) {
		AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();
		System.out.println("Using credentials: " + credentialsProvider.resolveCredentials());

		SpringApplication.run(SpringBootAwsS3TestApplication.class, args);
	}

}
