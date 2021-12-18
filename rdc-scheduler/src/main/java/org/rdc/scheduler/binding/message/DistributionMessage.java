package org.rdc.scheduler.binding.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@Data
public class DistributionMessage<T> {
    private UUID correlationID;
    String instanceName;
    private DistributionEventType type;
    Boolean rdcValid;
    private T content;
}
