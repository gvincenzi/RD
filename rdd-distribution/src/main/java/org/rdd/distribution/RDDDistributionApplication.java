package org.rdd.distribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class RDDDistributionApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDDDistributionApplication.class, args);
    }
}
