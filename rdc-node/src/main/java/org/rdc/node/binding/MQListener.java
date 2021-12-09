package org.rdc.node.binding;

import lombok.extern.java.Log;
import org.rdc.node.binding.message.DistributionEventType;
import org.rdc.node.binding.message.DistributionMessage;
import org.rdc.node.binding.message.entity.EntryProposition;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.item.RDCItem;
import org.rdc.node.service.impl.RDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@Log
@EnableBinding(MQBinding.class)
public class MQListener {
    @Autowired
    RDCItemService rdcItemService;

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
                //TODO Publish RDC ITEM added on bus
            }
        }
    }
}
