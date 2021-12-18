package org.rdc.distribution.domain.service.valence;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.ItemProposition;
import org.rdc.distribution.exception.RDCDistributionException;

public interface DeliveryValenceService {
    DistributionMessage<ItemProposition> proposeItem(ItemProposition itemProposition) throws RDCDistributionException;
    DistributionMessage<Void> sendIntegrityVerificationRequest() throws RDCDistributionException;
}
