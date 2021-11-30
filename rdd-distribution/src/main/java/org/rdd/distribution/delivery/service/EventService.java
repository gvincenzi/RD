package org.rdd.distribution.delivery.service;

import org.rdd.distribution.binding.message.DistributionEventType;
import org.rdd.distribution.domain.entity.EntryProposition;

public interface EventService {
    Boolean sendEntryProposition(DistributionEventType eventType, EntryProposition entryProposition);
}
