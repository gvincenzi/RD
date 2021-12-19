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
        log.info(String.format("[DISTRIBUTION SPIKE] Item proposition received"));
        DistributionMessage<ItemProposition> distributionMessage = null;
        try {
            distributionMessage = deliveryValenceService.proposeItem(itemProposition);
        } catch (RDCDistributionException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(distributionMessage, HttpStatus.GATEWAY_TIMEOUT);
        }
        return distributionMessage.getCorrelationID() != null ?
            new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
            new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/verify")
    public ResponseEntity<DistributionMessage<Void>> integrityVerification() {
        log.info(String.format("[DISTRIBUTION SPIKE] Integrity verification request received"));
        DistributionMessage<Void> distributionMessage = null;
        try {
            distributionMessage = deliveryValenceService.sendIntegrityVerificationRequest();
        } catch (RDCDistributionException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(distributionMessage, HttpStatus.GATEWAY_TIMEOUT);
        }
        try {
            ControllerResponseCache.putInCache(distributionMessage);
        } catch (RDCDistributionException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(distributionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return distributionMessage.getCorrelationID() != null ?
                new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
                new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/verify/internal")
    public ResponseEntity<DistributionMessage<Void>> integrityVerificationInternal() {
        log.info(String.format("[DISTRIBUTION SPIKE] Internal integrity verification request received"));
        DistributionMessage<Void> distributionMessage = null;
        try {
            distributionMessage = deliveryValenceService.sendIntegrityVerificationRequest();
        } catch (RDCDistributionException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(distributionMessage, HttpStatus.GATEWAY_TIMEOUT);
        }
        return distributionMessage.getCorrelationID() != null ?
                new ResponseEntity<>(distributionMessage, HttpStatus.OK) :
                new ResponseEntity<>(distributionMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<DistributionMessage> get(@PathVariable UUID uuid) {
        log.info(String.format("[DISTRIBUTION SPIKE] Get asynchronous result request received with Correlation ID [%s]",uuid.toString()));
        if(ControllerResponseCache.getFromCache(uuid) != null){
            return new ResponseEntity<>(ControllerResponseCache.removeFromCache(uuid), HttpStatus.OK);
        } else return new ResponseEntity(uuid, HttpStatus.NO_CONTENT);
    }

}
