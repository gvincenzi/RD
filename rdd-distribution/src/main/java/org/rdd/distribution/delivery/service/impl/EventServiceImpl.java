package org.rdd.distribution.delivery.service.impl;

import org.rdd.distribution.binding.message.DistributionEventType;
import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.delivery.service.EventService;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    MessageChannel requestChannel;

    @Override
    public DistributionMessage sendEntryProposition(EntryProposition entryProposition) {
        DistributionMessage distributionMessage = new DistributionMessage<EntryProposition>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.ENTRY_PROPOSITION);
        distributionMessage.setContent(entryProposition);
        Message<DistributionMessage> msg = MessageBuilder.withPayload(distributionMessage).build();
        requestChannel.send(msg);

        return distributionMessage;
    }

    @Override
    public DistributionMessage<Void> sendListEntriesRequest() {
        DistributionMessage distributionMessage = new DistributionMessage<Void>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.LIST_ENTRIES_REQUEST);
        Message<DistributionMessage> msg = MessageBuilder.withPayload(distributionMessage).build();
        requestChannel.send(msg);

        return distributionMessage;
    }

    @Override
    public DistributionMessage<Void> sendIntegrityVerificationRequest() {
        DistributionMessage distributionMessage = new DistributionMessage<Void>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        Message<DistributionMessage> msg = MessageBuilder.withPayload(distributionMessage).build();
        requestChannel.send(msg);

        return distributionMessage;
    }
}

