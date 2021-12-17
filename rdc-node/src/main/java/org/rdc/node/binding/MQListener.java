package org.rdc.node.binding;

import lombok.extern.java.Log;
import org.rdc.node.binding.message.DistributionEventType;
import org.rdc.node.binding.message.DistributionMessage;
import org.rdc.node.binding.message.entity.ItemProposition;
import org.rdc.node.core.entity.RDCItem;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.core.service.RDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
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
            if (rdcItem != null) {
                items.add(rdcItem);
                log.info("Item added with ID : " + rdcItem.getId());
            }
            DistributionMessage<List<RDCItem>> responseMessage = new DistributionMessage<>();
            responseMessage.setCorrelationID(msg.getCorrelationID());
            responseMessage.setInstanceName(instanceName);
            responseMessage.setType(DistributionEventType.ENTRY_RESPONSE);
            responseMessage.setContent(items);
            Message<DistributionMessage<List<RDCItem>>> responseMsg = MessageBuilder.withPayload(responseMessage).build();
            responseChannel.send(responseMsg);
        }
    }

    @StreamListener(target = "distributionChannel")
    public void processDistribution(DistributionMessage<List<RDCItem>> msg) {
        if (DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null && !instanceName.equals(msg.getInstanceName())) {
            try {
                for (RDCItem item : msg.getContent()) {
                    if (rdcItemService.forceAddItem(item)) {
                        log.info("RDC Item correctly validated");
                    } else {
                        corruptionDetected(msg);
                    }
                }
            } catch (RDCNodeException e) {
                log.severe(e.getMessage());
            }
        } else if (DistributionEventType.INTEGRITY_VERIFICATION.equals(msg.getType()) && msg.getContent() != null && !instanceName.equals(msg.getInstanceName())) {
            try {
                rdcItemService.init(msg.getContent());
                log.info("RDC correctly validated");
            } catch (RDCNodeException e) {
                log.severe(e.getMessage());
                corruptionDetected(msg);
            }
        } else if (DistributionEventType.CORRUPTION_DETECTED.equals(msg.getType()) && msg.getContent() != null) {
            try {
                for (RDCItem item : msg.getContent()) {
                    rdcItemService.forceAddItem(item);
                }
            } catch (RDCNodeException e) {
                log.severe(e.getMessage());
            }
        }
    }

    private void corruptionDetected(DistributionMessage<?> msg) {
        DistributionMessage<List<RDCItem>> responseMessage = new DistributionMessage<>();
        responseMessage.setCorrelationID(msg.getCorrelationID());
        responseMessage.setInstanceName(instanceName);
        responseMessage.setType(DistributionEventType.CORRUPTION_DETECTED);
        Message<DistributionMessage<List<RDCItem>>> responseMsg = MessageBuilder.withPayload(responseMessage).build();
        responseChannel.send(responseMsg);
    }

    @StreamListener(target = "requestIntegrityChannel")
    public void processIntegrity(DistributionMessage<Void> msg) {
        if (DistributionEventType.INTEGRITY_VERIFICATION.equals(msg.getType())) {
            try {
                List<RDCItem> items = rdcItemService.findAll();
                Boolean validation = rdcItemService.validate(items);
                if (!validation) {
                    corruptionDetected(msg);
                }
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
