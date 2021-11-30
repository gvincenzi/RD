package org.rdd.distribution.delivery.service.valence.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rdd.distribution.binding.message.DistributionEventType;
import org.rdd.distribution.delivery.service.EventService;
import org.rdd.distribution.domain.entity.Document;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.entity.Participant;
import org.rdd.distribution.domain.service.DistributionService;
import org.rdd.distribution.domain.service.valence.DeliveryValenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DeliveryServiceTest {
    private static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    DeliveryValenceService deliveryValenceService;

    @MockBean
    EventService eventService;

    protected static Document getNewDocument(String json) throws JsonProcessingException {
        log.info(json);
        Document document = mapper.readValue(json, Document.class);
        log.info(mapper.writeValueAsString(document));

        return document;
    }

    private EntryProposition getEntryProposition() throws JsonProcessingException {
        EntryProposition entryProposition = new EntryProposition();
        String json = "{\"title\":\"Test document\",\"countryName\":\"Italy\","
                + "\"countryPopulation\":60591668,\"male\":29665645,\"female\":30921362}";
        entryProposition.setDocument(getNewDocument(json));
        Participant owner = new Participant();
        owner.setMail("test@test.com");
        entryProposition.setOwner(owner);
        return entryProposition;
    }

    @Test
    public void addNewEntry() throws JsonProcessingException {
        EntryProposition entryProposition = getEntryProposition();
        Mockito.when(eventService.sendEntryProposition(DistributionEventType.ENTRY_PROPOSITION, entryProposition)).thenReturn(Boolean.TRUE);
        Boolean proposed = deliveryValenceService.addNewEntry(entryProposition);
        AssertionErrors.assertEquals("Error in Entry object proposition reception", Boolean.TRUE, proposed);
    }
}
