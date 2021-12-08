package org.rdc.scheduler.binding;

import org.rdc.scheduler.binding.message.DistributionEventType;
import org.rdc.scheduler.domain.entity.Entry;
import org.rdc.scheduler.notifier.valence.NotifierValenceService;
import org.rdc.scheduler.binding.message.DistributionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(MQBinding.class)
public class MQListener {
    @Autowired
    NotifierValenceService notifierValenceService;

    @StreamListener(target = "responseChannel")
    public void processEntryResponse(DistributionMessage<Entry> msg) {
        if(DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null){
            notifierValenceService.sendEntryResponseMail(msg.getContent(), msg.getContent().getOwner());
        }
    }
}
