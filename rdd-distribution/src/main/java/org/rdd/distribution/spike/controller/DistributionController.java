package org.rdd.distribution.spike.controller;

import lombok.extern.java.Log;
import org.rdd.distribution.binding.message.DistributionMessage;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
public class DistributionController {
    @Autowired
    DistributionService distributionService;

    @PostMapping("/entry/proposition")
    public ResponseEntity<DistributionMessage> entryProposition(@RequestBody EntryProposition entryProposition) {
        DistributionMessage distributionMessage = distributionService.addNewEntry(entryProposition);
        ResponseEntity responseEntity =  distributionMessage.getCorrelationID() != null ?
            new ResponseEntity(distributionMessage, HttpStatus.OK) :
            new ResponseEntity(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
        return responseEntity;
    }

    @PostMapping("/entry/list")
    public ResponseEntity<DistributionMessage> getListOfAllExistingEntries() {
        DistributionMessage distributionMessage = distributionService.getListOfAllExistingEntries();
        ResponseEntity responseEntity =  distributionMessage.getCorrelationID() != null ?
                new ResponseEntity(distributionMessage, HttpStatus.OK) :
                new ResponseEntity(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
        return responseEntity;
    }

    @PostMapping("/verify")
    public ResponseEntity<DistributionMessage> integrityVerification() {
        DistributionMessage distributionMessage = distributionService.verifyRegistryIntegrity();
        ResponseEntity responseEntity =  distributionMessage.getCorrelationID() != null ?
                new ResponseEntity(distributionMessage, HttpStatus.OK) :
                new ResponseEntity(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
        return responseEntity;
    }

}
