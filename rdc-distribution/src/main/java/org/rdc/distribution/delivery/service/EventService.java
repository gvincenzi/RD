package org.rdc.distribution.delivery.service;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.EntryProposition;

public interface EventService {
    DistributionMessage<EntryProposition> sendEntryProposition(EntryProposition entryProposition);
    DistributionMessage<Void> sendListEntriesRequest();
    DistributionMessage<Void> sendIntegrityVerificationRequest();
}
