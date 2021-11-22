package org.rdd.distribution.domain.service.impl;

import org.rdd.distribution.domain.entity.Entry;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.service.DeliveryValenceService;
import org.rdd.distribution.domain.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DistributionServiceImpl implements DistributionService {
    @Autowired
    DeliveryValenceService deliveryValenceService;

    @Override
    public Entry addNewEntry(EntryProposition entryProposition) {
        return deliveryValenceService.addNewEntry(entryProposition);
    }

    @Override
    public Set<Entry> getListOfAllExistingEntries() {
        return deliveryValenceService.getListOfAllExistingEntries();
    }

    @Override
    public Boolean verifyRegistryIntegrity() {
        return deliveryValenceService.verifyRegistryIntegrity();
    }
}
