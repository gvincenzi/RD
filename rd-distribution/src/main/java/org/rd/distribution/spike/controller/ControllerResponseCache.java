package org.rd.distribution.spike.controller;

import org.rd.distribution.binding.message.DistributionMessage;
import org.rd.distribution.exception.RDDistributionException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControllerResponseCache {
    private static int limitCache = 100;
    private static volatile Map<UUID, DistributionMessage> cache = new HashMap<>(limitCache);

    public static void putInCache(DistributionMessage distributionMessage) throws RDDistributionException {
        if(cache.size()<limitCache){
            cache.put(distributionMessage.getCorrelationID(),distributionMessage);
        } else {
            throw new RDDistributionException(String.format("Max cache limit [%d] exceeded",limitCache));
        }
    }

    public static DistributionMessage getFromCache(UUID correlationId) {
        return cache.get(correlationId);
    }

    public static DistributionMessage removeFromCache(UUID correlationId) {
        return cache.remove(correlationId);
    }
}
