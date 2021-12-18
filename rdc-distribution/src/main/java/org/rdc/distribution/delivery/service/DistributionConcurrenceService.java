package org.rdc.distribution.delivery.service;

import org.rdc.distribution.exception.RDCDistributionException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class DistributionConcurrenceService {
    private static volatile Set<UUID> correlationIDs = new HashSet<>();
    private static volatile UUID lastBlockingCorrelationID;

    public static Set<UUID> getCorrelationIDs() {
        return correlationIDs;
    }

    public static UUID getLastBlockingCorrelationID() {
        return lastBlockingCorrelationID;
    }

    public static void setLastBlockingCorrelationID(UUID lastBlockingCorrelationID) {
        DistributionConcurrenceService.lastBlockingCorrelationID = lastBlockingCorrelationID;
    }

    public abstract void waitingForLastCorrelationIDProcessing() throws RDCDistributionException;
}
