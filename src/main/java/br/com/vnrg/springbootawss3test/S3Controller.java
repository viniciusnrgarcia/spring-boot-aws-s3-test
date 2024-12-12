package br.com.vnrg.springbootawss3test;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Client s3Client;

    public S3Controller(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        String bucketName = "my-local-bucket";

        try {
            // Create a temp file for LocalStack upload
            Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), ".csv");
            file.transferTo(tempFile.toFile());

            // Upload to S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(file.getOriginalFilename())
                            .build(),
                    tempFile);

            // Cleanup temporary file
            Files.delete(tempFile);

            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed!");
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        String bucketName = "my-local-bucket";

        try {
            // List all objects in the bucket
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            // Extract file names
            List<String> fileNames = response.contents()
                    .stream()
                    .map(S3Object::key)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(fileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("fileName") String fileName) {
        String bucketName = "my-local-bucket";

        try {
            // Fetch the file from the S3 bucket
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            InputStream inputStream = s3Client.getObject(getObjectRequest);

            // Return the file as a downloadable resource
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }
}
