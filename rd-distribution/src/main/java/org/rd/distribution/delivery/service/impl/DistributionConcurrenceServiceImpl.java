package org.rd.distribution.delivery.service.impl;

import lombok.extern.java.Log;
import org.rd.distribution.delivery.service.DistributionConcurrenceService;
import org.rd.distribution.exception.RDDistributionException;
import org.springframework.stereotype.Service;

@Log
@Service
public class DistributionConcurrenceServiceImpl extends DistributionConcurrenceService {
    @Override
    public synchronized void waitingForLastCorrelationIDProcessing() throws RDDistributionException {
        int numberOfTry = 0;
        while(getCorrelationIDs().contains(getLastBlockingCorrelationID())){
            log.info(String.format("Waiting last blocking correlationID process end - CorrelationID [%s]",getLastBlockingCorrelationID().toString()));
            if(numberOfTry == 3){
                throw new RDDistributionException(String.format("Timeout while last blocking correlationID process end waiting  - CorrelationID [%s]",getLastBlockingCorrelationID().toString()));
            }
            try {
                numberOfTry++;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.severe(e.getMessage());
            }
        }
        log.info("No blocking correlationID in progress");
    }
}
