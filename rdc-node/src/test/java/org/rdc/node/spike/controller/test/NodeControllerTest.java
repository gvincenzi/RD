package org.rdc.node.spike.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.rdc.node.binding.message.entity.Document;
import org.rdc.node.binding.message.entity.Participant;
import org.rdc.node.core.entity.RDCItem;
import org.rdc.node.core.repository.RDCItemRepository;
import org.rdc.node.core.service.RDCItemService;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.spike.client.SpikeClient;
import org.rdc.node.spike.controller.NodeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Log
@RunWith(SpringRunner.class)
@WebMvcTest(NodeController.class)
@ActiveProfiles("test")
public class NodeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    RDCItemService rdcItemService;

    @MockBean
    RDCItemRepository rdcItemRepository;

    @MockBean
    SpikeClient spikeClient;

    @Test
    public void welcome() throws Exception {
        Mockito.when(rdcItemService.findAll()).thenReturn(new ArrayList<>());
        mvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void init() throws Exception {
        mvc.perform(get("/init"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
