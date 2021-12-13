package org.rdc.node.configuration;

import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.service.RDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class NodeConfig {
    @Autowired
    RDCItemService itemService;

    @Value("${required.startup}")
    private Boolean requiredStartup;

    @PostConstruct
    void getStartup() throws RDCNodeException {
        if(requiredStartup){
            itemService.startup();
        }
    }
}
