package org.rdc.distribution.domain.service;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.ItemProposition;

public interface DistributionService {
    DistributionMessage<ItemProposition> proposeItem(ItemProposition itemProposition);
    DistributionMessage<Void> getListOfAllExistingEntries();
    DistributionMessage<Void> verifyRegistryIntegrity();
}
