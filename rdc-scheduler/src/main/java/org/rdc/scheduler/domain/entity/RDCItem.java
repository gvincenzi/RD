package org.rdc.scheduler.domain.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class RDCItem {
    String id;
    String previousId;
    Instant timestamp;
    Integer nonce;
    Document document;
    Participant owner;
    String nodeInstanceName;
    Boolean isCorruptionDetected = Boolean.FALSE;

}
