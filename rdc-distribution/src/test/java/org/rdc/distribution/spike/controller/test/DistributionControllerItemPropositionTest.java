package org.rdc.distribution.spike.controller.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rdc.distribution.binding.message.DistributionEventType;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.Participant;
import org.rdc.distribution.domain.service.valence.DeliveryValenceService;
import org.rdc.distribution.spike.controller.DistributionController;
import org.rdc.distribution.domain.entity.Document;
import org.rdc.distribution.domain.entity.ItemProposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@RunWith(SpringRunner.class)
@WebMvcTest(DistributionController.class)
@ActiveProfiles("test")
public class DistributionControllerItemPropositionTest {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mvc;

    @MockBean
    DeliveryValenceService deliveryValenceService;

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

    @WithMockUser(value = "test")
    @Test
    public void entryOk() throws Exception {
        ItemProposition itemProposition = getEntryProposition();
        DistributionMessage<ItemProposition> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.ENTRY_PROPOSITION);
        distributionMessage.setContent(itemProposition);
        Mockito.when(deliveryValenceService.proposeItem(itemProposition)).thenReturn(distributionMessage);
        mvc.perform(post("/item/proposition")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(itemProposition)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(value = "test")
    @Test
    public void entryKo() throws Exception {
        ItemProposition itemProposition = getEntryProposition();
        DistributionMessage<ItemProposition> distributionMessage = new DistributionMessage<>();
        distributionMessage.setType(DistributionEventType.ENTRY_PROPOSITION);
        distributionMessage.setContent(itemProposition);
        Mockito.when(deliveryValenceService.proposeItem(itemProposition)).thenReturn(distributionMessage);
        mvc.perform(post("/item/proposition")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(itemProposition)))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @WithAnonymousUser
    @Test
    public void entryNoUser() throws Exception {
        ItemProposition itemProposition = getEntryProposition();
        DistributionMessage<ItemProposition> distributionMessage = new DistributionMessage<>();
        distributionMessage.setType(DistributionEventType.ENTRY_PROPOSITION);
        distributionMessage.setContent(itemProposition);
        Mockito.when(deliveryValenceService.proposeItem(itemProposition)).thenReturn(distributionMessage);
        mvc.perform(post("/item/proposition")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(itemProposition)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
