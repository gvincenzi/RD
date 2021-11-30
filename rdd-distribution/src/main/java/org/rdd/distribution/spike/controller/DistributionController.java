package org.rdd.distribution.spike.controller;

import lombok.extern.java.Log;
import org.rdd.distribution.domain.entity.EntryProposition;
import org.rdd.distribution.domain.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
public class DistributionController {
    @Autowired
    DistributionService distributionService;

    @PostMapping("/entry")
    public ResponseEntity<String> entryProposition(@RequestBody EntryProposition entryProposition) {
        ResponseEntity responseEntity = distributionService.addNewEntry(entryProposition) ?
            new ResponseEntity("Entry proposition correctly submitted", HttpStatus.OK) :
            new ResponseEntity("Error submitting entry proposition", HttpStatus.NOT_ACCEPTABLE);
        return responseEntity;
    }

}
