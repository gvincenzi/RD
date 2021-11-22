package org.rdd.distribution.delivery.service.valence.impl;

import lombok.extern.java.Log;
import org.rdd.distribution.domain.entity.Entry;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.service.valence.DeliveryValenceService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log
@Service
public class DeliveryValenceServiceImpl implements DeliveryValenceService {
    @Override
    public Entry addNewEntry(EntryProposition entryProposition) {
        return null;
    }

    @Override
    public Set<Entry> getListOfAllExistingEntries() {
        return null;
    }

    @Override
    public Boolean verifyRegistryIntegrity() {
        return null;
    }
}
