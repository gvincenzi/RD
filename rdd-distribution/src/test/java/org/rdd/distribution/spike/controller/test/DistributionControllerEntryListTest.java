package org.rdd.distribution.spike.controller.test;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rdd.distribution.binding.message.DistributionEventType;
import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.domain.service.DistributionService;
import org.rdd.distribution.spike.controller.DistributionController;
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
public class DistributionControllerEntryListTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    DistributionService distributionService;

    @WithMockUser(value = "test")
    @Test
    public void entryOk() throws Exception {
        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.LIST_ENTRIES_REQUEST);
        Mockito.when(distributionService.getListOfAllExistingEntries()).thenReturn(distributionMessage);
        mvc.perform(post("/entry/list")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(value = "test")
    @Test
    public void entryKo() throws Exception {
        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setType(DistributionEventType.LIST_ENTRIES_REQUEST);
        Mockito.when(distributionService.getListOfAllExistingEntries()).thenReturn(distributionMessage);
        mvc.perform(post("/entry/list")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @WithAnonymousUser
    @Test
    public void entryNoUser() throws Exception {
        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setType(DistributionEventType.LIST_ENTRIES_REQUEST);
        Mockito.when(distributionService.getListOfAllExistingEntries()).thenReturn(distributionMessage);
        mvc.perform(post("/entry/list")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
