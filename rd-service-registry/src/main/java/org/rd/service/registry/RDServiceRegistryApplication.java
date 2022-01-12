package org.rd.service.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RDServiceRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDServiceRegistryApplication.class, args);
    }
}
