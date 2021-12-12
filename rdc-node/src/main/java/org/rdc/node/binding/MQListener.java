package org.rdc.node.binding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.rdc.node.binding.message.DistributionEventType;
import org.rdc.node.binding.message.DistributionMessage;
import org.rdc.node.binding.message.entity.ItemProposition;
import org.rdc.node.domain.entity.RDCItem;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.service.impl.RDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

@Log
@EnableBinding(MQBinding.class)
public class MQListener {
    @Autowired
    RDCItemService rdcItemService;

    @Autowired
    MessageChannel responseChannel;

    @Value("${spring.application.name}")
    private String instanceName;

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @StreamListener(target = "requestChannel")
    public void processItemProposition(DistributionMessage<ItemProposition> msg) {
        if (DistributionEventType.ENTRY_PROPOSITION.equals(msg.getType()) && msg.getContent() != null) {
            List<RDCItem> items = new ArrayList<>();
            RDCItem rdcItem = null;
            try {
                rdcItem = rdcItemService.add(msg.getContent().getDocument(), msg.getContent().getOwner());
            } catch (RDCNodeException e) {
                log.severe(e.getMessage());
            }
            items.add(rdcItem);

            if (rdcItem != null) {
                log.info("Item added with ID : " + rdcItem.getId());
                DistributionMessage<List<RDCItem>> responseMessage = new DistributionMessage<>();
                responseMessage.setCorrelationID(msg.getCorrelationID());
                responseMessage.setInstanceName(instanceName);
                responseMessage.setType(DistributionEventType.ENTRY_RESPONSE);
                responseMessage.setContent(items);
                Message<DistributionMessage<List<RDCItem>>> responseMsg = MessageBuilder.withPayload(responseMessage).build();
                responseChannel.send(responseMsg);
            }
        }
    }

    @StreamListener(target = "distributionChannel")
    public void processDistribution(DistributionMessage<List<RDCItem>> msg) {
        if (DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null && !instanceName.equals(msg.getInstanceName())) {
            try {
                if (rdcItemService.forceAddItem(msg.getContent().iterator().next())) {
                    log.info("RDC correctly validated");
                } else {
                    log.severe("RDC is corrupted");
                }
            } catch (RDCNodeException e) {
                log.severe(e.getMessage());
            }
        }
    }

    @StreamListener(target = "requestIntegrityChannel")
    public void processIntegrity(DistributionMessage<Void> msg) {
        if (DistributionEventType.INTEGRITY_VERIFICATION.equals(msg.getType())) {
            try {
                List<RDCItem> items = rdcItemService.findAll();
                Boolean validation = rdcItemService.validate(items);
                DistributionMessage<List<RDCItem>> responseMessage = new DistributionMessage<>();
                responseMessage.setCorrelationID(msg.getCorrelationID());
                responseMessage.setInstanceName(instanceName);
                responseMessage.setType(DistributionEventType.INTEGRITY_VERIFICATION);
                responseMessage.setRdcValid(validation);
                responseMessage.setContent(items);
                Message<DistributionMessage<List<RDCItem>>> responseMsg = MessageBuilder.withPayload(responseMessage).build();
                responseChannel.send(responseMsg);
            } catch (RDCNodeException e) {
                log.severe(e.getMessage());
            }
        }
    }
}
