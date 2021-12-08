package org.rdc.distribution.domain.service.valence;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.EntryProposition;

public interface DeliveryValenceService {
    DistributionMessage<EntryProposition> addNewEntry(EntryProposition entryProposition);
    DistributionMessage<Void> getListOfAllExistingEntries();
    DistributionMessage<Void> verifyRegistryIntegrity();
}
