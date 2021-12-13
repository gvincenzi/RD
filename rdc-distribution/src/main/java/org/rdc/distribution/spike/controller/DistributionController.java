package org.rdc.distribution.spike.controller;

import lombok.extern.java.Log;
import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.domain.entity.ItemProposition;
import org.rdc.distribution.domain.service.valence.DeliveryValenceService;
import org.rdc.distribution.exception.RDCDistributionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log
@RestController
public class DistributionController {
    @Autowired
    DeliveryValenceService deliveryValenceService;

    @PostMapping("/item/proposition")
    public ResponseEntity<DistributionMessage<ItemProposition>> itemProposition(@RequestBody ItemProposition itemProposition) {
        DistributionMessage<ItemProposition> distributionMessage = deliveryValenceService.proposeItem(itemProposition);
        return distributionMessage.getCorrelationID() != null ?
            new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
            new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/verify")
    public ResponseEntity<DistributionMessage<Void>> integrityVerification() throws RDCDistributionException {
        DistributionMessage<Void> distributionMessage = deliveryValenceService.sendIntegrityVerificationRequest();
        ControllerResponseCache.putInCache(distributionMessage);
        return distributionMessage.getCorrelationID() != null ?
                new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
                new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<DistributionMessage> get(@PathVariable UUID uuid) {
        if(ControllerResponseCache.getFromCache(uuid) != null){
            return new ResponseEntity<>(ControllerResponseCache.removeFromCache(uuid), HttpStatus.OK);
        } else return new ResponseEntity(uuid, HttpStatus.NO_CONTENT);
    }

}
