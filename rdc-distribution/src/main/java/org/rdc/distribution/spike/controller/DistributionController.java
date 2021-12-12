package org.rdc.distribution.spike.controller;

import lombok.extern.java.Log;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.ItemProposition;
import org.rdc.distribution.domain.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log
@RestController
public class DistributionController {
    @Autowired
    DistributionService distributionService;

    @PostMapping("/item/proposition")
    public ResponseEntity<DistributionMessage<ItemProposition>> itemProposition(@RequestBody ItemProposition itemProposition) {
        DistributionMessage<ItemProposition> distributionMessage = distributionService.proposeItem(itemProposition);
        return distributionMessage.getCorrelationID() != null ?
            new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
            new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/verify")
    public ResponseEntity<DistributionMessage<Void>> integrityVerification() {
        DistributionMessage<Void> distributionMessage = distributionService.verifyRegistryIntegrity();
        ControllerResponseCache.cache.put(distributionMessage.getCorrelationID(),null);
        return distributionMessage.getCorrelationID() != null ?
                new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
                new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> get(@PathVariable UUID uuid) {
        if(ControllerResponseCache.cache.get(uuid) != null){
            return new ResponseEntity<>(ControllerResponseCache.cache.remove(uuid), HttpStatus.OK);
        } else return new ResponseEntity<>(uuid, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/item/list")
    public ResponseEntity<DistributionMessage<Void>> getListOfAllExistingItems() {
        DistributionMessage<Void> distributionMessage = distributionService.getListOfAllExistingEntries();
        ControllerResponseCache.cache.put(distributionMessage.getCorrelationID(),null);
        return distributionMessage.getCorrelationID() != null ?
                new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
                new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

}
