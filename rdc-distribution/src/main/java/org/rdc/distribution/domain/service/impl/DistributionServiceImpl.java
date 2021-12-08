package org.rdc.distribution.domain.service.impl;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.EntryProposition;
import org.rdc.distribution.domain.service.DistributionService;
import org.rdc.distribution.domain.service.valence.DeliveryValenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributionServiceImpl implements DistributionService {
    @Autowired
    DeliveryValenceService deliveryValenceService;

    @Override
    public DistributionMessage<EntryProposition> addNewEntry(EntryProposition entryProposition) {
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
