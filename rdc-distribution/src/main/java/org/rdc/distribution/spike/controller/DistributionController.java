package org.rdc.distribution.spike.controller;

import lombok.extern.java.Log;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.ItemProposition;
import org.rdc.distribution.domain.service.DistributionService;
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

    @PostMapping("/item/proposition")
    public ResponseEntity<DistributionMessage<ItemProposition>> itemProposition(@RequestBody ItemProposition itemProposition) {
        DistributionMessage<ItemProposition> distributionMessage = distributionService.addNewEntry(itemProposition);
        return distributionMessage.getCorrelationID() != null ?
            new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
            new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/item/list")
    public ResponseEntity<DistributionMessage<Void>> getListOfAllExistingItems() {
        DistributionMessage<Void> distributionMessage = distributionService.getListOfAllExistingEntries();
        return distributionMessage.getCorrelationID() != null ?
                new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
                new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/verify")
    public ResponseEntity<DistributionMessage<Void>> integrityVerification() {
        DistributionMessage<Void> distributionMessage = distributionService.verifyRegistryIntegrity();
        return distributionMessage.getCorrelationID() != null ?
                new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
                new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

}
