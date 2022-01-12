package org.rd.scheduler.notifier.valence.impl;

import org.apache.commons.lang.StringUtils;
import org.rd.scheduler.domain.entity.RDItem;
import org.rd.scheduler.domain.entity.Participant;
import org.rd.scheduler.notifier.valence.NotifierValenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NotifierValenceServiceImpl implements NotifierValenceService {
    @Autowired
    public JavaMailSender javaMailSender;

    @Autowired
    SimpleMailMessage templateEntryMessage;

    @Value("${template.subject.entry}")
    public String templateEntrySubject;

    @Autowired
    SimpleMailMessage templateCorruptionMessage;

    @Value("${template.subject.corruption}")
    public String templateCorruptionSubject;

    @Override
    public void sendEntryResponseMail(RDItem rdcItem, Participant... participants) {
        for (Participant participant : participants) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(participant.getMail());
            message.setSubject(templateEntrySubject);
            message.setText(templateEntryMessage != null && templateEntryMessage.getText() != null ? String.format(templateEntryMessage.getText(), rdcItem.getDocument()) : StringUtils.EMPTY);
            javaMailSender.send(message);
        }
    }

    @Override
    public void sendCorruptionMail(Set<Participant> participants) {
        for (Participant participant : participants) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(participant.getMail());
            message.setSubject(templateCorruptionSubject);
            message.setText(templateCorruptionMessage != null && templateCorruptionMessage.getText() != null ? templateCorruptionMessage.getText() : StringUtils.EMPTY);
            javaMailSender.send(message);
        }
    }
}
