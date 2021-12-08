package org.rdc.scheduler.notifier.valence;

import org.rdc.scheduler.domain.entity.Entry;
import org.rdc.scheduler.domain.entity.Participant;

public interface NotifierValenceService {
    void sendEntryResponseMail(Entry entry, Participant ... participants);
}
