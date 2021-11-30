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

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    MessageChannel entryPropositionChannel;

    @Override
    public Boolean sendEntryProposition(DistributionEventType eventType, EntryProposition entryProposition) {
        DistributionMessage distributionMessage = new DistributionMessage();
        distributionMessage.setEntryProposition(entryProposition);
        Message<DistributionMessage> msg = MessageBuilder.withPayload(distributionMessage).build();
        Boolean result = Boolean.FALSE;

        switch (eventType){
            case ENTRY_PROPOSITION: result = entryPropositionChannel.send(msg); break;
        }

        return result;
    }
}

