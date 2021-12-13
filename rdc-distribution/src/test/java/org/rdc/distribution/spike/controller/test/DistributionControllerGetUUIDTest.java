package org.rdc.distribution.spike.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rdc.distribution.binding.message.DistributionEventType;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.service.DistributionService;
import org.rdc.distribution.spike.controller.ControllerResponseCache;
import org.rdc.distribution.spike.controller.DistributionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@RunWith(SpringRunner.class)
@WebMvcTest(DistributionController.class)
@ActiveProfiles("test")
public class DistributionControllerGetUUIDTest {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mvc;

    @MockBean
    DistributionService distributionService;

    @Test
    public void noContent() throws Exception {
        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        Mockito.when(distributionService.verifyRegistryIntegrity()).thenReturn(distributionMessage);
        mvc.perform(get("/"+distributionMessage.getCorrelationID()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void ok() throws Exception {
        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        ControllerResponseCache.cache.put(distributionMessage.getCorrelationID(),distributionMessage);
        Mockito.when(distributionService.verifyRegistryIntegrity()).thenReturn(distributionMessage);
        MvcResult mvcResult = mvc.perform(get("/" + distributionMessage.getCorrelationID()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(mapper.writeValueAsString(distributionMessage),content);
    }
}
