package org.rdc.scheduler.binding.message;

import lombok.Data;

import java.util.UUID;

@Data
public class DistributionMessage<T> {
    UUID correlationID;
    String instanceName;
    DistributionEventType type;
    T content;
}
