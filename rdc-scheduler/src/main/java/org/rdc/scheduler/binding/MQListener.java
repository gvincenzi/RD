package org.rdc.scheduler.binding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.rdc.scheduler.binding.message.DistributionEventType;
import org.rdc.scheduler.domain.entity.RDCItem;
import org.rdc.scheduler.notifier.valence.NotifierValenceService;
import org.rdc.scheduler.binding.message.DistributionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Log
@EnableBinding(MQBinding.class)
public class MQListener {
    @Autowired
    NotifierValenceService notifierValenceService;

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @StreamListener(target = "responseChannel")
    public void processEntryResponse(DistributionMessage<List<RDCItem>> msg) {
        log.info(String.format("START >> Message received in Response Channel with Correlation ID [%s]",msg.getCorrelationID()));
        if(DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null){
            for (RDCItem rdcItem: msg.getContent()) {
                if(rdcItem.getOwner() != null && rdcItem.getOwner().getMail() != null){
                    notifierValenceService.sendEntryResponseMail(rdcItem,rdcItem.getOwner());
                }
            }
        }
        log.info(String.format("END >> Message received in Response Channel with Correlation ID [%s]",msg.getCorrelationID()));
    }
}
