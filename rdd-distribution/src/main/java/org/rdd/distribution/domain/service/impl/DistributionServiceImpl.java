package org.rdd.distribution.domain.service.impl;

import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.service.DistributionService;
import org.rdd.distribution.domain.service.valence.DeliveryValenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributionServiceImpl implements DistributionService {
    @Autowired
    DeliveryValenceService deliveryValenceService;

    @Override
    public DistributionMessage addNewEntry(EntryProposition entryProposition) {
        return deliveryValenceService.addNewEntry(entryProposition);
    }

    @Override
    public DistributionMessage<Void> getListOfAllExistingEntries() {
        return deliveryValenceService.getListOfAllExistingEntries();
    }

    @Override
    public DistributionMessage<Void> verifyRegistryIntegrity() {
        return deliveryValenceService.verifyRegistryIntegrity();
    }
}
