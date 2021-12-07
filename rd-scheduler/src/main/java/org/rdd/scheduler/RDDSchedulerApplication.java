package org.rdd.scheduler;

import org.rdd.scheduler.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableEurekaClient
@EnableBinding(MQBinding.class)
@SpringBootApplication
public class RDDSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDDSchedulerApplication.class, args);
    }
}
