package org.rdc.distribution;

import org.rdc.distribution.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableEurekaClient
@EnableBinding(MQBinding.class)
@SpringBootApplication
public class RDCDistributionApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDCDistributionApplication.class, args);
    }
}
