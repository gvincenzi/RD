package org.rdc.scheduler.notifier.valence.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rdc.scheduler.domain.entity.Document;
import org.rdc.scheduler.domain.entity.RDCItem;
import org.rdc.scheduler.notifier.valence.NotifierValenceService;
import org.rdc.scheduler.domain.entity.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.UUID;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NotifierValenceServiceTest {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    NotifierValenceService notifierValenceService;

    @MockBean
    JavaMailSender javaMailSender;

    protected static Document getNewDocument(String json) throws JsonProcessingException {
        log.info(json);
        Document document = mapper.readValue(json, Document.class);
        log.info(mapper.writeValueAsString(document));

        return document;
    }

    private RDCItem getEntry() throws JsonProcessingException {
        RDCItem RDCItem = new RDCItem();
        String json = "{\"title\":\"Test document\",\"countryName\":\"Italy\","
                + "\"countryPopulation\":60591668,\"male\":29665645,\"female\":30921362}";
        RDCItem.setDocument(getNewDocument(json));
        Participant owner = new Participant();
        owner.setMail("test@test.com");
        RDCItem.setOwner(owner);
        RDCItem.setId(UUID.randomUUID().toString());
        RDCItem.setTimestamp(Instant.now());
        return RDCItem;
    }

    @Test
    public void sendEntryResponseMail() throws JsonProcessingException {
        notifierValenceService.sendEntryResponseMail(getEntry(),getEntry().getOwner());
    }
}
