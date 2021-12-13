package org.rdc.distribution.delivery.service.valence.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rdc.distribution.binding.message.DistributionEventType;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.delivery.service.DistributionConcurrenceService;
import org.rdc.distribution.delivery.service.EventService;
import org.rdc.distribution.domain.entity.Document;
import org.rdc.distribution.domain.entity.ItemProposition;
import org.rdc.distribution.domain.entity.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTest {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    EventService eventService;

    @MockBean
    @Qualifier("requestChannel")
    MessageChannel requestChannel;

    @MockBean
    @Qualifier("requestIntegrityChannel")
    MessageChannel requestIntegrityChannel;

    @MockBean
    @Qualifier("responseChannel")
    SubscribableChannel responseChannel;

    @MockBean
    DistributionConcurrenceService distributionConcurrenceService;

    protected static Document getNewDocument(String json) throws JsonProcessingException {
        log.info(json);
        Document document = mapper.readValue(json, Document.class);
        log.info(mapper.writeValueAsString(document));

        return document;
    }

    private ItemProposition getEntryProposition() throws JsonProcessingException {
        ItemProposition itemProposition = new ItemProposition();
        String json = "{\"title\":\"Test document\",\"countryName\":\"Italy\","
                + "\"countryPopulation\":60591668,\"male\":29665645,\"female\":30921362}";
        itemProposition.setDocument(getNewDocument(json));
        Participant owner = new Participant();
        owner.setMail("test@test.com");
        itemProposition.setOwner(owner);
        return itemProposition;
    }

    @Test
    public void addNewEntry() throws JsonProcessingException {
        ItemProposition itemProposition = getEntryProposition();
        Mockito.when(requestChannel.send(Mockito.any(Message.class))).thenReturn(Boolean.TRUE);

        DistributionMessage<ItemProposition> proposed = eventService.sendItemProposition(itemProposition);
        AssertionErrors.assertNotNull("Correlation ID is null", proposed.getCorrelationID());
        AssertionErrors.assertEquals("DistributionType is not coherent", DistributionEventType.ENTRY_PROPOSITION,proposed.getType());
        AssertionErrors.assertEquals("EntryProposition is not equal", itemProposition, proposed.getContent());

    }

    @Test
    public void sendIntegrityVerificationRequest() {
        Mockito.when(requestChannel.send(Mockito.any(Message.class))).thenReturn(Boolean.TRUE);

        DistributionMessage<Void> proposed = eventService.sendIntegrityVerificationRequest();
        AssertionErrors.assertNotNull("Correlation ID is null", proposed.getCorrelationID());
        AssertionErrors.assertEquals("DistributionType is not coherent", DistributionEventType.INTEGRITY_VERIFICATION,proposed.getType());

    }
}
