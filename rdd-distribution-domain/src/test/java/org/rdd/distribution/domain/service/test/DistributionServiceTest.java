package org.rdd.distribution.domain.service.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rdd.distribution.domain.entity.Document;
import org.rdd.distribution.domain.entity.Entry;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.entity.Participant;
import org.rdd.distribution.domain.service.DeliveryValenceService;
import org.rdd.distribution.domain.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DistributionServiceTest {
    private static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockBean
    DeliveryValenceService deliveryValenceService;

    @Autowired
    DistributionService distributionService;

    protected static Document getNewDocument(String json) throws JsonProcessingException {
        log.info(json);
        Document document = mapper.readValue(json, Document.class);
        log.info(mapper.writeValueAsString(document));

        return document;
    }

    @Test
    public void addNewEntryTest() throws JsonProcessingException {
        EntryProposition entryProposition = new EntryProposition();
        String json = "{\"title\":\"Test document\",\"countryName\":\"Italy\","
                + "\"countryPopulation\":60591668,\"male\":29665645,\"female\":30921362}";
        entryProposition.setDocument(getNewDocument(json));
        Participant owner = new Participant();
        owner.setMail("test@test.com");
        entryProposition.setOwner(owner);

        Entry entryValidated = new Entry();
        String jsonValidated = "{\"title\":\"Test document\",\"countryName\":\"Italy\","
                + "\"countryPopulation\":60591668,\"male\":29665645,\"female\":30921362}";
        entryValidated.setDocument(getNewDocument(jsonValidated));
        Participant ownerValidated = new Participant();
        ownerValidated.setMail("test@test.com");
        entryValidated.setOwner(owner);
        entryValidated.setId((new Random()).nextLong());
        entryValidated.setInsertionDate(Instant.now());
        Mockito.when(deliveryValenceService.addNewEntry(entryProposition)).thenReturn(entryValidated);

        Entry newEntry = distributionService.addNewEntry(entryProposition);
        AssertionErrors.assertEquals("Error in Entry object validated reception", mapper.writeValueAsString(newEntry),mapper.writeValueAsString(entryValidated));
    }
}
