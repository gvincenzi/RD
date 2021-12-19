package org.rdc.node.spike.controller;

import lombok.extern.java.Log;
import org.rdc.node.core.configuration.StartupConfig;
import org.rdc.node.core.entity.RDCItem;
import org.rdc.node.core.service.RDCItemService;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.spike.client.SpikeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Log
@Controller
public class NodeController {
    @Value("${spring.application.name}")
    private String instanceName;

    @Autowired
    RDCItemService rdcItemService;

    @Autowired
    SpikeClient spikeClient;

    @GetMapping("/")
    public String welcome(Model model) throws RDCNodeException {
        List<RDCItem> items = rdcItemService.findAll();
        model.addAttribute("instanceName", instanceName);
        model.addAttribute("validation", rdcItemService.validate(items));
        model.addAttribute("startup", StartupConfig.startupProcessed);
        model.addAttribute("items", items);

        return "welcome"; //view
    }

    @GetMapping("/init")
    public String init(Model model) {
        spikeClient.integrityVerification();
        model.addAttribute("instanceName", instanceName);
        return "afterInit"; //view
    }
}
