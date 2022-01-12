package org.rd.node.spike.controller.test;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rd.node.core.repository.RDItemRepository;
import org.rd.node.spike.client.SpikeClient;
import org.rd.node.spike.controller.NodeController;
import org.rd.node.core.service.RDItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

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
    RDItemService RDItemService;

    @MockBean
    RDItemRepository RDItemRepository;

    @MockBean
    SpikeClient spikeClient;

    @Test
    public void welcome() throws Exception {
        Mockito.when(RDItemService.findAll()).thenReturn(new ArrayList<>());
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
