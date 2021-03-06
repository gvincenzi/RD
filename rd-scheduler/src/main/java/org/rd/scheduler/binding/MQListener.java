package org.rd.scheduler.binding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.rd.scheduler.binding.message.DistributionEventType;
import org.rd.scheduler.binding.message.DistributionMessage;
import org.rd.scheduler.notifier.valence.NotifierValenceService;
import org.rd.scheduler.domain.entity.RDItem;
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
    public void processEntryResponse(DistributionMessage<List<RDItem>> msg) {
        log.info(String.format("START >> Message received in Response Channel with Correlation ID [%s]",msg.getCorrelationID()));
        if(DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null){
            for (RDItem rdItem: msg.getContent()) {
                if(rdItem.getOwner() != null && rdItem.getOwner().getMail() != null){
                    notifierValenceService.sendEntryResponseMail(rdItem,rdItem.getOwner());
                }
            }
        }
        log.info(String.format("END >> Message received in Response Channel with Correlation ID [%s]",msg.getCorrelationID()));
    }
}
