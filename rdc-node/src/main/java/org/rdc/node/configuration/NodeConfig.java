package org.rdc.node.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.service.IRDCItemService;
import org.rdc.node.service.impl.RDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class NodeConfig {
    @Autowired
    IRDCItemService itemService;

    @PostConstruct
    void getStartup() throws RDCNodeException {
        itemService.startup();
    }
}
