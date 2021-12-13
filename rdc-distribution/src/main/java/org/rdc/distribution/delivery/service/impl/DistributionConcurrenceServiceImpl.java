package org.rdc.distribution.delivery.service.impl;

import lombok.extern.java.Log;
import org.rdc.distribution.delivery.service.DistributionConcurrenceService;
import org.springframework.stereotype.Service;

@Log
@Service
public class DistributionConcurrenceServiceImpl extends DistributionConcurrenceService {
    @Override
    public void waitingForLastCorrelationIDProcessing() {
        while(getCorrelationIDs().contains(getLastBlockingCorrelationID())){
            log.info("Waiting last correlationID process");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.severe(e.getMessage());
            }
        }
    }
}
