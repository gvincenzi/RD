package org.rdc.distribution.binding.message;

import lombok.Data;

import java.util.UUID;

@Data
public class DistributionMessage<T> {
    private UUID correlationID;
    private DistributionEventType type;
    private T content;
}
