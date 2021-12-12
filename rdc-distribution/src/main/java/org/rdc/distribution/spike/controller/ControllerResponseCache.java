package org.rdc.distribution.spike.controller;

import org.rdc.distribution.binding.message.DistributionMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControllerResponseCache {
    public static volatile Map<UUID, DistributionMessage> cache = new HashMap<>();
}
