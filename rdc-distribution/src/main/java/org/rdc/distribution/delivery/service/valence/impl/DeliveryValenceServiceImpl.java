package org.rdc.distribution.delivery.service.valence.impl;

import lombok.extern.java.Log;
import org.rdc.distribution.binding.message.DistributionEventType;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.delivery.service.DistributionConcurrenceService;
import org.rdc.distribution.domain.entity.ItemProposition;
import org.rdc.distribution.domain.service.valence.DeliveryValenceService;
import org.rdc.distribution.exception.RDCDistributionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log
@Service
public class DeliveryValenceServiceImpl implements DeliveryValenceService {
    @Autowired
    MessageChannel requestChannel;

    @Autowired
    MessageChannel requestIntegrityChannel;

    @Autowired
    DistributionConcurrenceService distributionConcurrenceService;

    @Override
    public DistributionMessage<ItemProposition> proposeItem(ItemProposition itemProposition) throws RDCDistributionException {
        distributionConcurrenceService.waitingForLastCorrelationIDProcessing();

        DistributionMessage<ItemProposition> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.ENTRY_PROPOSITION);
        distributionMessage.setContent(itemProposition);
        Message<DistributionMessage<ItemProposition>> msg = MessageBuilder.withPayload(distributionMessage).build();
        requestChannel.send(msg);
        DistributionConcurrenceService.setLastBlockingCorrelationID(distributionMessage.getCorrelationID());
        DistributionConcurrenceService.getCorrelationIDs().add(DistributionConcurrenceService.getLastBlockingCorrelationID());
        log.info(String.format("Correlation ID [%s] waiting for processing",distributionMessage.getCorrelationID().toString()));
        return distributionMessage;
    }

    private DistributionMessage<Void> getVoidDistributionMessage(DistributionEventType listEntriesRequest) throws RDCDistributionException{
        distributionConcurrenceService.waitingForLastCorrelationIDProcessing();

        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(listEntriesRequest);
        Message<DistributionMessage<Void>> msg = MessageBuilder.withPayload(distributionMessage).build();
        requestIntegrityChannel.send(msg);
        DistributionConcurrenceService.setLastBlockingCorrelationID(distributionMessage.getCorrelationID());
        DistributionConcurrenceService.getCorrelationIDs().add(DistributionConcurrenceService.getLastBlockingCorrelationID());
        return distributionMessage;
    }

    @Override
    public DistributionMessage<Void> sendIntegrityVerificationRequest() throws RDCDistributionException {
        return getVoidDistributionMessage(DistributionEventType.INTEGRITY_VERIFICATION);
    }
}
