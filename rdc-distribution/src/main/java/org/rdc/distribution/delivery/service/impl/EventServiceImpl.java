package org.rdc.distribution.delivery.service.impl;

import lombok.extern.java.Log;
import org.rdc.distribution.binding.MQListener;
import org.rdc.distribution.binding.message.DistributionEventType;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.delivery.service.EventService;
import org.rdc.distribution.domain.entity.EntryProposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log
@Service
public class EventServiceImpl implements EventService {
    @Autowired
    MessageChannel requestChannel;

    UUID lastCorrelationID;

    @Override
    public DistributionMessage<EntryProposition> sendEntryProposition(EntryProposition entryProposition) {
        while(MQListener.correlationIDs.contains(lastCorrelationID)){
            log.info("Waiting last correlationID process");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.severe(e.getMessage());
            }
        }

        DistributionMessage<EntryProposition> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.ENTRY_PROPOSITION);
        distributionMessage.setContent(entryProposition);
        Message<DistributionMessage<EntryProposition>> msg = MessageBuilder.withPayload(distributionMessage).build();
        requestChannel.send(msg);
        lastCorrelationID = distributionMessage.getCorrelationID();
        MQListener.correlationIDs.add(lastCorrelationID);
        log.info(String.format("Correlation ID [%s] waiting for processing",distributionMessage.getCorrelationID().toString()));
        return distributionMessage;
    }

    private DistributionMessage<Void> getVoidDistributionMessage(DistributionEventType listEntriesRequest) {
        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(listEntriesRequest);
        Message<DistributionMessage<Void>> msg = MessageBuilder.withPayload(distributionMessage).build();
        requestChannel.send(msg);

        return distributionMessage;
    }

    @Override
    public DistributionMessage<Void> sendListEntriesRequest() {
        return getVoidDistributionMessage(DistributionEventType.LIST_ENTRIES_REQUEST);
    }

    @Override
    public DistributionMessage<Void> sendIntegrityVerificationRequest() {
        return getVoidDistributionMessage(DistributionEventType.INTEGRITY_VERIFICATION);
    }
}

