package org.rd.distribution;

import org.rd.distribution.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableEurekaClient
@EnableBinding(MQBinding.class)
@SpringBootApplication
public class RDDistributionApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDDistributionApplication.class, args);
    }
}
