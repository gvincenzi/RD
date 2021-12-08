package org.rdc.distribution.delivery.service.valence.impl;

import lombok.extern.java.Log;
import org.rdc.distribution.domain.service.valence.DeliveryValenceService;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.delivery.service.EventService;
import org.rdc.distribution.domain.entity.EntryProposition;
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
