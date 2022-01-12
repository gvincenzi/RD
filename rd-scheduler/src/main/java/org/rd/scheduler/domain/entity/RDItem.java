package org.rd.scheduler.domain.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class RDItem {
    String id;
    String previousId;
    Instant timestamp;
    Integer nonce;
    Document document;
    Participant owner;
    String nodeInstanceName;
    Boolean isCorruptionDetected = Boolean.FALSE;

}
