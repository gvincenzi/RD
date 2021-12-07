package org.rdd.scheduler.binding;

import org.rdd.scheduler.binding.message.DistributionEventType;
import org.rdd.scheduler.binding.message.DistributionMessage;
import org.rdd.scheduler.domain.entity.Entry;
import org.rdd.scheduler.notifier.valence.NotifierValenceService;
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
