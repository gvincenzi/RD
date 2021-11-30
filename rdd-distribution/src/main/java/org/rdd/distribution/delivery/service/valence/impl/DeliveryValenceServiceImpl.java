package org.rdd.distribution.delivery.service.valence.impl;

import lombok.extern.java.Log;
import org.rdd.distribution.binding.message.DistributionEventType;
import org.rdd.distribution.delivery.service.EventService;
import org.rdd.distribution.domain.entity.Entry;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.service.valence.DeliveryValenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log
@Service
public class DeliveryValenceServiceImpl implements DeliveryValenceService {
    @Autowired
    EventService eventService;

    @Override
    public Boolean addNewEntry(EntryProposition entryProposition) {
        return eventService.sendEntryProposition(DistributionEventType.ENTRY_PROPOSITION, entryProposition);
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
