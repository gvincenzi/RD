package org.rdc.node.binding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.rdc.node.binding.message.DistributionEventType;
import org.rdc.node.binding.message.DistributionMessage;
import org.rdc.node.binding.message.entity.Entry;
import org.rdc.node.binding.message.entity.EntryProposition;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.item.RDCItem;
import org.rdc.node.service.impl.RDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@Log
@EnableBinding(MQBinding.class)
public class MQListener {
    @Autowired
    RDCItemService rdcItemService;

    @Autowired
    MessageChannel responseChannel;

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @StreamListener(target = "requestChannel")
    public void processEntryResponse(DistributionMessage<EntryProposition> msg) {
        if(DistributionEventType.ENTRY_PROPOSITION.equals(msg.getType()) && msg.getContent() != null){
            RDCItem rdcItem = null;
            try {
                rdcItem = rdcItemService.add(msg.getContent().getDocument(), msg.getContent().getOwner());
            } catch (RDCNodeException e) {
                log.severe(e.getMessage());
            }

            if(rdcItem != null) {
                log.info("Item added with ID : " + rdcItem.getId());
                DistributionMessage<Entry> responseMessage = new DistributionMessage<>();
                responseMessage.setCorrelationID(msg.getCorrelationID());
                responseMessage.setType(DistributionEventType.ENTRY_RESPONSE);
                Entry entry = new Entry();
                entry.setDocument(msg.getContent().getDocument());
                entry.setId(rdcItem.getId());
                entry.setInsertionInstant(rdcItem.getTimestamp());
                entry.setOwner(msg.getContent().getOwner());
                responseMessage.setContent(entry);
                Message<DistributionMessage<Entry>> responseMsg = MessageBuilder.withPayload(responseMessage).build();
                responseChannel.send(responseMsg);
            }
        }
    }
}
