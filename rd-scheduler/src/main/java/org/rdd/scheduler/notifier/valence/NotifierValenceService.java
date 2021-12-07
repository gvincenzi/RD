package org.rdd.scheduler.notifier.valence;

import org.rdd.scheduler.domain.entity.Entry;
import org.rdd.scheduler.domain.entity.Participant;

public interface NotifierValenceService {
    void sendEntryResponseMail(Entry entry, Participant ... participants);
}
