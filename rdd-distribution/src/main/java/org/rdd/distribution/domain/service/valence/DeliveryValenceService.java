package org.rdd.distribution.domain.service.valence;

import org.rdd.distribution.domain.entity.Entry;
import org.rdd.distribution.domain.entity.EntryProposition;

import java.util.Set;

public interface DeliveryValenceService {
    Entry addNewEntry(EntryProposition entryProposition);
    Set<Entry> getListOfAllExistingEntries();
    Boolean verifyRegistryIntegrity();
}
