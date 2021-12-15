package org.rdc.node.controller;

import org.rdc.node.domain.entity.RDCItem;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.service.RDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NodeController {
    @Value("${spring.application.name}")
    private String instanceName;

    @Autowired
    RDCItemService rdcItemService;

    @GetMapping("/")
    public String main(Model model) throws RDCNodeException {
        List<RDCItem> items = rdcItemService.findAll();
        model.addAttribute("instanceName", instanceName);
        model.addAttribute("validation", rdcItemService.validate(items));
        model.addAttribute("items", items);

        return "welcome"; //view
    }
}
