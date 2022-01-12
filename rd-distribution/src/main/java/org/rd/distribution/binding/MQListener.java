package org.rd.distribution.binding;

import lombok.extern.java.Log;
import org.rd.distribution.delivery.service.DistributionConcurrenceService;
import org.rd.distribution.domain.entity.RDItem;
import org.rd.distribution.spike.controller.ControllerResponseCache;
import org.rd.distribution.binding.message.DistributionEventType;
import org.rd.distribution.binding.message.DistributionMessage;
import org.rd.distribution.exception.RDDistributionException;
import org.springframework.beans.factory.annotation.Autowired;
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
    MessageChannel distributionChannel;

    @StreamListener(target = "responseChannel")
    public void processEntryResponse(DistributionMessage<List<RDItem>> msg) {
        log.info(String.format("START >> Message received in Response Channel with Correlation ID [%s]",msg.getCorrelationID()));
        if(DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null){
            log.info(String.format("Correlation ID [%s] processed",msg.getCorrelationID()));
            DistributionConcurrenceService.getCorrelationIDs().remove(msg.getCorrelationID());
            Message<DistributionMessage<List<RDItem>>> message = MessageBuilder.withPayload(msg).build();
            distributionChannel.send(message);
        } else if(DistributionEventType.INTEGRITY_VERIFICATION.equals(msg.getType()) && msg.getContent() != null){
            log.info(String.format("Correlation ID [%s] processed",msg.getCorrelationID()));
            DistributionConcurrenceService.getCorrelationIDs().remove(msg.getCorrelationID());
            try {
                ControllerResponseCache.putInCache(msg);
            } catch (RDDistributionException e) {
                log.severe(e.getMessage());
            }
            Message<DistributionMessage<List<RDItem>>> message = MessageBuilder.withPayload(msg).build();
            distributionChannel.send(message);
        } else if(DistributionEventType.CORRUPTION_DETECTED.equals(msg.getType())){
            log.info(String.format("Correlation ID [%s] processed",msg.getCorrelationID()));
            DistributionConcurrenceService.getCorrelationIDs().remove(msg.getCorrelationID());

            List<RDItem> rdItems = new ArrayList<>();
            rdItems.add(RDItem.getRdItemCorruption());
            msg.setContent(rdItems);
            Message<DistributionMessage<List<RDItem>>> message = MessageBuilder.withPayload(msg).build();
            distributionChannel.send(message);
        }
        log.info(String.format("END >> Message received in Response Channel with Correlation ID [%s]",msg.getCorrelationID()));
    }
}
