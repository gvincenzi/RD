package org.rdc.distribution.domain.service;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.EntryProposition;

public interface DistributionService {
    DistributionMessage<EntryProposition> addNewEntry(EntryProposition entryProposition);
    DistributionMessage<Void> getListOfAllExistingEntries();
    DistributionMessage<Void> verifyRegistryIntegrity();
}
