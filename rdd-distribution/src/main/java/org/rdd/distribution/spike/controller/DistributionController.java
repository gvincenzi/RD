package org.rdd.distribution.spike.controller;

import lombok.extern.java.Log;
import org.rdd.distribution.domain.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
public class DistributionController {
    @Autowired
    DistributionService distributionService;

    @GetMapping("/hi")
    public ResponseEntity<String> hi() {
        return new ResponseEntity("Hi all, this is a molecule spike", HttpStatus.OK);
    }
}
