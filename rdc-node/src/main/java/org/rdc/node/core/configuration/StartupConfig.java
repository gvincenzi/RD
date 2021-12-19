package org.rdc.node.core.configuration;

import lombok.extern.java.Log;
import org.rdc.node.spike.client.SpikeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Log
@Configuration
public class StartupConfig {
    public static volatile Boolean startupProcessed = Boolean.FALSE;

    @Autowired
    SpikeClient spikeClient;

    @Value("${rdc.startup}")
    private Boolean requiredStartup;

    @PostConstruct
    void getStartup() {
        if(requiredStartup){
            log.info("Startup node required");
            spikeClient.integrityVerification();
            log.info("Integrity verification request sent");
        } else {
            log.info("Startup node not required");
            startupProcessed = Boolean.TRUE;
        }
    }
}
