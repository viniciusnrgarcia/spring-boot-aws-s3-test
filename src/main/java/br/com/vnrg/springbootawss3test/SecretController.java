package br.com.vnrg.springbootawss3test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sm")
public class SecretController {

    @Value("${secrets.username}")
    public String username;

    @Value("${secrets.password}")
    public String password;

    @GetMapping
    public ResponseEntity<String> listSecrets() {
        try {
            return ResponseEntity.ok(String.join(",", username, password));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed!");
        }
    }

}
