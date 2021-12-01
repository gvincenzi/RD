package org.rdd.distribution.domain.service;

import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.domain.entity.EntryProposition;

public interface DistributionService {
    DistributionMessage<EntryProposition> addNewEntry(EntryProposition entryProposition);
    DistributionMessage<Void> getListOfAllExistingEntries();
    DistributionMessage<Void> verifyRegistryIntegrity();
}
