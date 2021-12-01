package org.rdd.distribution.delivery.service.valence.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rdd.distribution.binding.message.DistributionEventType;
import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.delivery.service.EventService;
import org.rdd.distribution.domain.entity.Document;
import org.rdd.distribution.domain.entity.Entry;
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

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DeliveryValenceServiceTest {
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
        DistributionMessage distributionMessage = new DistributionMessage<EntryProposition>();
        distributionMessage.setContent(entryProposition);
        distributionMessage.setType(DistributionEventType.ENTRY_PROPOSITION);
        distributionMessage.setCorrelationID(UUID.randomUUID());
        Mockito.when(eventService.sendEntryProposition(entryProposition)).thenReturn(distributionMessage);
        DistributionMessage proposed = deliveryValenceService.addNewEntry(entryProposition);
        AssertionErrors.assertNotNull("Correlation ID is null", proposed.getCorrelationID());
        AssertionErrors.assertEquals("DistributionType is not coherent", DistributionEventType.ENTRY_PROPOSITION,proposed.getType());
        AssertionErrors.assertEquals("EntryProposition is not equal", entryProposition, proposed.getContent());
    }

    @Test
    public void getListOfAllExistingEntries() {
        DistributionMessage distributionMessage = new DistributionMessage<EntryProposition>();
        distributionMessage.setType(DistributionEventType.LIST_ENTRIES_REQUEST);
        distributionMessage.setCorrelationID(UUID.randomUUID());
        Mockito.when(eventService.sendListEntriesRequest()).thenReturn(distributionMessage);
        DistributionMessage<Void> proposed = deliveryValenceService.getListOfAllExistingEntries();
        AssertionErrors.assertNotNull("Correlation ID is null", proposed.getCorrelationID());
        AssertionErrors.assertEquals("DistributionType is not coherent", DistributionEventType.LIST_ENTRIES_REQUEST,proposed.getType());
    }

    @Test
    public void verifyRegistryIntegrity() {
        DistributionMessage distributionMessage = new DistributionMessage<Void>();
        distributionMessage.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        distributionMessage.setCorrelationID(UUID.randomUUID());
        Mockito.when(eventService.sendIntegrityVerificationRequest()).thenReturn(distributionMessage);
        DistributionMessage<Void> proposed = deliveryValenceService.verifyRegistryIntegrity();
        AssertionErrors.assertNotNull("Correlation ID is null", proposed.getCorrelationID());
        AssertionErrors.assertEquals("DistributionType is not coherent", DistributionEventType.INTEGRITY_VERIFICATION,proposed.getType());
    }
}
