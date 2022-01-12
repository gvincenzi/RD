package org.rd.scheduler.notifier.valence;

import org.rd.scheduler.domain.entity.RDItem;
import org.rd.scheduler.domain.entity.Participant;

import java.util.Set;

public interface NotifierValenceService {
    void sendEntryResponseMail(RDItem RDCItem, Participant ... participants);
    void sendCorruptionMail(Set<Participant> participants);
}
