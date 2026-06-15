package br.com.copa.noticiaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class NoticiaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoticiaServiceApplication.class, args);
    }
}