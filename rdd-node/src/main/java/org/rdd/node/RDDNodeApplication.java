package org.rdd.node;

import org.rdd.node.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableEurekaClient
@EnableBinding(MQBinding.class)
@SpringBootApplication
public class RDDNodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDDNodeApplication.class, args);
    }
}
