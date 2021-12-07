package org.rdd.scheduler.notifier.valence.impl;

import org.apache.commons.lang.StringUtils;
import org.rdd.scheduler.domain.entity.Entry;
import org.rdd.scheduler.domain.entity.Participant;
import org.rdd.scheduler.notifier.valence.NotifierValenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotifierValenceServiceImpl implements NotifierValenceService {
    @Autowired
    public JavaMailSender javaMailSender;

    @Autowired
    SimpleMailMessage templateEntryMessage;

    @Value("${template.subject.entry}")
    public String templateEntrySubject;

    @Override
    public void sendEntryResponseMail(Entry entry, Participant... participants) {
        for (Participant participant : participants) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(participant.getMail());
            message.setSubject(templateEntrySubject);
            message.setText(templateEntryMessage != null && templateEntryMessage.getText() != null ? String.format(templateEntryMessage.getText(), entry.getDocument().getTitle()) : StringUtils.EMPTY);
            javaMailSender.send(message);
        }
    }
}
