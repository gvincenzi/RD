package org.rdc.node.core.configuration;

import org.rdc.node.spike.client.SpikeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class StartupConfig {
    @Autowired
    SpikeClient spikeClient;

    @Value("${rdc.startup}")
    private Boolean requiredStartup;

    @PostConstruct
    void getStartup() {
        if(requiredStartup){
            spikeClient.integrityVerification();
        }
    }
}
