package org.rdd.distribution.domain.service.valence;

import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.domain.entity.EntryProposition;

public interface DeliveryValenceService {
    DistributionMessage<EntryProposition> addNewEntry(EntryProposition entryProposition);
    DistributionMessage<Void> getListOfAllExistingEntries();
    DistributionMessage<Void> verifyRegistryIntegrity();
}
