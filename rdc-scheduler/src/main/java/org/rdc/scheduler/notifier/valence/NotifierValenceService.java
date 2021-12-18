package org.rdc.scheduler.notifier.valence;

import org.rdc.scheduler.domain.entity.RDCItem;
import org.rdc.scheduler.domain.entity.Participant;

import java.util.Set;

public interface NotifierValenceService {
    void sendEntryResponseMail(RDCItem RDCItem, Participant ... participants);
    void sendCorruptionMail(Set<Participant> participants);
}
