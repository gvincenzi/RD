package org.rd.node.binding;

import lombok.extern.java.Log;
import org.rd.node.binding.message.DistributionEventType;
import org.rd.node.core.configuration.StartupConfig;
import org.rd.node.binding.message.DistributionMessage;
import org.rd.node.binding.message.entity.ItemProposition;
import org.rd.node.core.entity.RDItem;
import org.rd.node.exception.RDNodeException;
import org.rd.node.core.service.RDItemService;
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
    RDItemService RDItemService;

    @Autowired
    MessageChannel responseChannel;

    @Value("${spring.application.name}")
    private String instanceName;

    @StreamListener(target = "requestChannel")
    public void processItemProposition(DistributionMessage<ItemProposition> msg) {
        log.info(String.format("START >> Message received in Request Channel with Correlation ID [%s]",msg.getCorrelationID()));
        if (DistributionEventType.ENTRY_PROPOSITION.equals(msg.getType()) && msg.getContent() != null && StartupConfig.startupProcessed) {
            List<RDItem> items = new ArrayList<>();
            RDItem rdItem = null;
            try {
                rdItem = RDItemService.add(msg.getContent().getDocument(), msg.getContent().getOwner());
            } catch (RDNodeException e) {
                log.severe(e.getMessage());
            }
            if (rdItem != null) {
                items.add(rdItem);
                log.info(String.format("New item with ID [%s] correctly validated and ingested", rdItem.getId()));
            }
            DistributionMessage<List<RDItem>> responseMessage = new DistributionMessage<>();
            responseMessage.setCorrelationID(msg.getCorrelationID());
            responseMessage.setInstanceName(instanceName);
            responseMessage.setType(DistributionEventType.ENTRY_RESPONSE);
            responseMessage.setContent(items);
            Message<DistributionMessage<List<RDItem>>> responseMsg = MessageBuilder.withPayload(responseMessage).build();
            responseChannel.send(responseMsg);
        }
        log.info(String.format("END >> Message received in Request Channel with Correlation ID [%s]",msg.getCorrelationID()));
    }

    @StreamListener(target = "distributionChannel")
    public void processDistribution(DistributionMessage<List<RDItem>> msg) {
        log.info(String.format("START >> Message received in Distribution Channel with Correlation ID [%s]",msg.getCorrelationID()));
        if (DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null && !instanceName.equals(msg.getInstanceName()) && StartupConfig.startupProcessed) {
            try {
                for (RDItem item : msg.getContent()) {
                    if (RDItemService.forceAddItem(item)) {
                        log.info(String.format("New item with ID [%s] correctly validated and ingested",item.getId()));
                    } else {
                        corruptionDetected(msg);
                    }
                }
            } catch (RDNodeException e) {
                log.severe(e.getMessage());
            }
        } else if (DistributionEventType.INTEGRITY_VERIFICATION.equals(msg.getType()) && msg.getContent() != null && !instanceName.equals(msg.getInstanceName())) {
            try {
                RDItemService.init(msg.getContent());

                if(Boolean.FALSE.equals(StartupConfig.startupProcessed)){
                    log.info("Startup process for this node has been correctly terminated");
                    StartupConfig.startupProcessed = Boolean.TRUE;
                }

                log.info("Integrity verification correctly validated and ingested");
            } catch (RDNodeException e) {
                log.severe(e.getMessage());
                corruptionDetected(msg);
            }
        } else if (DistributionEventType.CORRUPTION_DETECTED.equals(msg.getType()) && msg.getContent() != null && StartupConfig.startupProcessed) {
            try {
                for (RDItem item : msg.getContent()) {
                    RDItemService.forceAddItem(item);
                }
            } catch (RDNodeException e) {
                log.severe(e.getMessage());
            }
        }
        log.info(String.format("END >> Message received in Distribution Channel with Correlation ID [%s]",msg.getCorrelationID()));
    }

    private void corruptionDetected(DistributionMessage<?> msg) {
        log.warning(String.format("Corruption detected : send message with Correlation ID [%s]",msg.getCorrelationID()));
        DistributionMessage<List<RDItem>> responseMessage = new DistributionMessage<>();
        responseMessage.setCorrelationID(msg.getCorrelationID());
        responseMessage.setInstanceName(instanceName);
        responseMessage.setType(DistributionEventType.CORRUPTION_DETECTED);
        Message<DistributionMessage<List<RDItem>>> responseMsg = MessageBuilder.withPayload(responseMessage).build();
        responseChannel.send(responseMsg);
    }

    @StreamListener(target = "requestIntegrityChannel")
    public void processIntegrity(DistributionMessage<Void> msg) {
        if (DistributionEventType.INTEGRITY_VERIFICATION.equals(msg.getType())) {
            try {
                List<RDItem> items = RDItemService.findAll();
                Boolean validation = RDItemService.validate(items);
                if (!validation) {
                    corruptionDetected(msg);
                }
                DistributionMessage<List<RDItem>> responseMessage = new DistributionMessage<>();
                responseMessage.setCorrelationID(msg.getCorrelationID());
                responseMessage.setInstanceName(instanceName);
                responseMessage.setType(DistributionEventType.INTEGRITY_VERIFICATION);
                responseMessage.setRdValid(validation);
                responseMessage.setContent(items);
                Message<DistributionMessage<List<RDItem>>> responseMsg = MessageBuilder.withPayload(responseMessage).build();
                responseChannel.send(responseMsg);
            } catch (RDNodeException e) {
                log.severe(e.getMessage());
            }
        }
    }
}
