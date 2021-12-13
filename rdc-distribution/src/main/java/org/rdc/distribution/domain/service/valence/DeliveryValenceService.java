package org.rdc.distribution.domain.service.valence;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.ItemProposition;

public interface DeliveryValenceService {
    DistributionMessage<ItemProposition> proposeItem(ItemProposition itemProposition);
    DistributionMessage<Void> sendIntegrityVerificationRequest();
}
