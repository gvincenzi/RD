package org.rdc.distribution.delivery.service;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.ItemProposition;

public interface EventService {
    DistributionMessage<ItemProposition> sendEntryProposition(ItemProposition itemProposition);
    DistributionMessage<Void> sendListEntriesRequest();
    DistributionMessage<Void> sendIntegrityVerificationRequest();
}
