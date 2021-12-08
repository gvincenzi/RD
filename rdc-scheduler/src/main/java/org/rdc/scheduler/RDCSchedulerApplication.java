package org.rdc.scheduler;

import org.rdc.scheduler.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableEurekaClient
@EnableBinding(MQBinding.class)
@SpringBootApplication
public class RDCSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDCSchedulerApplication.class, args);
    }
}
