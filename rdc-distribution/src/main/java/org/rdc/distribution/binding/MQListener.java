package org.rdc.distribution.binding;

import lombok.extern.java.Log;
import org.rdc.distribution.binding.message.DistributionEventType;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.Entry;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@Log
@EnableBinding(MQBinding.class)
public class MQListener {
    public static Boolean lastCorrelationIDProcessed;

    @StreamListener(target = "responseChannel")
    public void processEntryResponse(DistributionMessage<Entry> msg) {
        if(DistributionEventType.ENTRY_RESPONSE.equals(msg.getType()) && msg.getContent() != null){
            log.info(String.format("Correlation ID [%s] processed",msg.getCorrelationID()));
            lastCorrelationIDProcessed = Boolean.TRUE;
        }
    }
}
