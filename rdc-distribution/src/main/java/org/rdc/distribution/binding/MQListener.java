package org.rdc.distribution.binding;

import lombok.extern.java.Log;
import org.rdc.distribution.binding.message.DistributionEventType;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.delivery.service.DistributionConcurrenceService;
import org.rdc.distribution.domain.entity.Document;
import org.rdc.distribution.domain.entity.Participant;
import org.rdc.distribution.domain.entity.RDCItem;
import org.rdc.distribution.exception.RDCDistributionException;
import org.rdc.distribution.spike.controller.ControllerResponseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Log
@EnableBinding(MQBinding.class)
public class MQListener {
    @Autowired
    MessageChannel distributionChannel;

    @StreamListener(target = "responseChannel")
    public void processEntryResponse(DistributionMessage<List<RDCItem>> msg) {
        log.info(String.format("START >> Message received in Response Channel with Correlation ID [%s]",msg.getCorrelationID()));
        if(DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null){
            log.info(String.format("Correlation ID [%s] processed",msg.getCorrelationID()));
            DistributionConcurrenceService.getCorrelationIDs().remove(msg.getCorrelationID());
            Message<DistributionMessage<List<RDCItem>>> message = MessageBuilder.withPayload(msg).build();
            distributionChannel.send(message);
        } else if(DistributionEventType.INTEGRITY_VERIFICATION.equals(msg.getType()) && msg.getContent() != null){
            log.info(String.format("Correlation ID [%s] processed",msg.getCorrelationID()));
            DistributionConcurrenceService.getCorrelationIDs().remove(msg.getCorrelationID());
            try {
                ControllerResponseCache.putInCache(msg);
            } catch (RDCDistributionException e) {
                log.severe(e.getMessage());
            }
            Message<DistributionMessage<List<RDCItem>>> message = MessageBuilder.withPayload(msg).build();
            distributionChannel.send(message);
        } else if(DistributionEventType.CORRUPTION_DETECTED.equals(msg.getType())){
            log.info(String.format("Correlation ID [%s] processed",msg.getCorrelationID()));
            DistributionConcurrenceService.getCorrelationIDs().remove(msg.getCorrelationID());

            List<RDCItem> rdcItems = new ArrayList<>();
            rdcItems.add(RDCItem.getRdcItemCorruption());
            msg.setContent(rdcItems);
            Message<DistributionMessage<List<RDCItem>>> message = MessageBuilder.withPayload(msg).build();
            distributionChannel.send(message);
        }
        log.info(String.format("END >> Message received in Response Channel with Correlation ID [%s]",msg.getCorrelationID()));
    }
}
