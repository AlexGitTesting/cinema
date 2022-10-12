package com.example.cinema;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(servers = @Server(url = "http://localhost:8080"), info = @Info(version = "1.0", title = "Cinema application Api", description = "Representation of the service which help you book seats in the cinema"))
public class CinemaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

}
