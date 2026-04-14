package ru.tomsknipi.track_gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrackGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackGatewayServiceApplication.class, args);
    }

}
