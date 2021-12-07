package org.rdd.scheduler.binding.message;

import lombok.Data;

import java.util.UUID;

@Data
public class DistributionMessage<T> {
    UUID correlationID;
    DistributionEventType type;
    T content;
}
