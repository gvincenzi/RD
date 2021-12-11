package org.rdc.distribution.binding;

import lombok.extern.java.Log;
import org.rdc.distribution.binding.message.DistributionEventType;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.RDCItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Log
@EnableBinding(MQBinding.class)
public class MQListener {
    public volatile static Set<UUID> correlationIDs = new HashSet<>();

    @Autowired
    MessageChannel distributionChannel;

    @StreamListener(target = "responseChannel")
    public void processEntryResponse(DistributionMessage<RDCItem> msg) {
        if(DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null){
            log.info(String.format("Correlation ID [%s] processed",msg.getCorrelationID()));
            correlationIDs.remove(msg.getCorrelationID());
            Message<DistributionMessage<RDCItem>> message = MessageBuilder.withPayload(msg).build();
            distributionChannel.send(message);
        }
    }
}
