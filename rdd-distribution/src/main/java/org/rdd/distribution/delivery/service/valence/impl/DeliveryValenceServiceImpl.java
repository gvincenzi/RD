package org.rdd.distribution.delivery.service.valence.impl;

import lombok.extern.java.Log;
import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.delivery.service.EventService;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.service.valence.DeliveryValenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log
@Service
public class DeliveryValenceServiceImpl implements DeliveryValenceService {
    @Autowired
    EventService eventService;

    @Override
    public DistributionMessage<EntryProposition> addNewEntry(EntryProposition entryProposition) {
        return eventService.sendEntryProposition(entryProposition);
    }

    @Override
    public DistributionMessage<Void> getListOfAllExistingEntries() {
        return eventService.sendListEntriesRequest();
    }

    @Override
    public DistributionMessage<Void> verifyRegistryIntegrity() {
        return eventService.sendIntegrityVerificationRequest();
    }
}
