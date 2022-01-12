package org.rd.distribution.domain.service.valence;

import org.rd.distribution.domain.entity.ItemProposition;
import org.rd.distribution.binding.message.DistributionMessage;
import org.rd.distribution.exception.RDDistributionException;

public interface DeliveryValenceService {
    DistributionMessage<ItemProposition> proposeItem(ItemProposition itemProposition) throws RDDistributionException;
    DistributionMessage<Void> sendIntegrityVerificationRequest() throws RDDistributionException;
}
