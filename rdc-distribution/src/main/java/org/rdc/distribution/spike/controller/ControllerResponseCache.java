package org.rdc.distribution.spike.controller;

import org.rdc.distribution.binding.message.DistributionMessage;
import org.rdc.distribution.exception.RDCDistributionException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControllerResponseCache {
    private static int limitCache = 100;
    private static volatile Map<UUID, DistributionMessage> cache = new HashMap<>(limitCache);

    public static void putInCache(DistributionMessage distributionMessage) throws RDCDistributionException {
        if(cache.size()<limitCache){
            cache.put(distributionMessage.getCorrelationID(),distributionMessage);
        } else {
            throw new RDCDistributionException("Max cache limit exceeded");
        }
    }

    public static DistributionMessage getFromCache(UUID correlationId) {
        return cache.get(correlationId);
    }

    public static DistributionMessage removeFromCache(UUID correlationId) {
        return cache.remove(correlationId);
    }
}
