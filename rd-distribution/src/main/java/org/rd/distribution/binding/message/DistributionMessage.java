package org.rd.distribution.binding.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(Include.NON_NULL)
public class DistributionMessage<T> {
    private UUID correlationID;
    String instanceName;
    private DistributionEventType type;
    Boolean rdcValid;
    private T content;
}
