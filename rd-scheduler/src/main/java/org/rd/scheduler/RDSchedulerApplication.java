package org.rd.scheduler;

import org.rd.scheduler.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@EnableBinding(MQBinding.class)
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class RDSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RDSchedulerApplication.class, args);
    }
}
