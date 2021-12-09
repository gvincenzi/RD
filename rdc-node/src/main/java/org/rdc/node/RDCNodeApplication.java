package org.rdc.node;

import org.rdc.node.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableEurekaClient
@EnableBinding(MQBinding.class)
@EnableMongoRepositories
@SpringBootApplication
public class RDCNodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDCNodeApplication.class, args);
    }
}
