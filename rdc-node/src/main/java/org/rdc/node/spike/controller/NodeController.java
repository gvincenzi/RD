package org.rdc.node.spike.controller;

import lombok.extern.java.Log;
import org.rdc.node.core.entity.RDCItem;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.core.service.RDCItemService;
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

    @GetMapping("/")
    public String welcome(Model model) throws RDCNodeException {
        List<RDCItem> items = rdcItemService.findAll();
        model.addAttribute("instanceName", instanceName);
        model.addAttribute("validation", rdcItemService.validate(items));
        model.addAttribute("items", items);

        return "welcome"; //view
    }

    @GetMapping("/startup")
    public String startup(Model model) throws RDCNodeException {
        long timeInMillis = System.currentTimeMillis();
        try{
            rdcItemService.startup();
            List<RDCItem> items = rdcItemService.findAll();
            model.addAttribute("validation", rdcItemService.validate(items));
            model.addAttribute("items", items);
        } catch (RDCNodeException e){
            List<RDCItem> items = rdcItemService.findAll();
            model.addAttribute("validation", false);
            model.addAttribute("items", items);
        } finally {
            model.addAttribute("instanceName", instanceName);
            model.addAttribute("startupTime",System.currentTimeMillis()-timeInMillis + " ms");
        }

        return "welcome"; //view
    }
}
