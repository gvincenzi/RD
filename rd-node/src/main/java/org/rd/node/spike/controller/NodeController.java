package org.rd.node.spike.controller;

import lombok.extern.java.Log;
import org.rd.node.core.configuration.StartupConfig;
import org.rd.node.spike.client.SpikeClient;
import org.rd.node.core.entity.RDItem;
import org.rd.node.core.service.RDItemService;
import org.rd.node.exception.RDNodeException;
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
    RDItemService RDItemService;

    @Autowired
    SpikeClient spikeClient;

    @GetMapping("/")
    public String welcome(Model model) throws RDNodeException {
        List<RDItem> items = RDItemService.findAll();
        model.addAttribute("instanceName", instanceName);
        model.addAttribute("validation", RDItemService.validate(items));
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
