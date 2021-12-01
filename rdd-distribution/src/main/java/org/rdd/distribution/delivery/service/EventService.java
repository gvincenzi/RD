package org.rdd.distribution.delivery.service;

import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.domain.entity.EntryProposition;

public interface EventService {
    DistributionMessage<EntryProposition> sendEntryProposition(EntryProposition entryProposition);
    DistributionMessage<Void> sendListEntriesRequest();
    DistributionMessage<Void> sendIntegrityVerificationRequest();
}
