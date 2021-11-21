package org.rdd.service.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RDDServiceRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDDServiceRegistryApplication.class, args);
    }
}
