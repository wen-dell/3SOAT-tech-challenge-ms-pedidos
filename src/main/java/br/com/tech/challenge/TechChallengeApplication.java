package br.com.tech.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TechChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechChallengeApplication.class, args);
    }

}
