package org.rdc.distribution.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class RDCItem implements Comparable<RDCItem>{
    String id;
    String previousId;
    Instant timestamp;
    Integer nonce;
    Document document;
    Participant owner;
    String nodeInstanceName;
    Boolean isCorruptionDetected = Boolean.FALSE;

    @Override
    public int compareTo(RDCItem arg0) {
        return getTimestamp().compareTo(arg0.getTimestamp());
    }

    public static RDCItem getRdcItemCorruption() {
        RDCItem rdcItemCorruption = new RDCItem();
        rdcItemCorruption.setId("CORRUPTION_DETECTION");
        rdcItemCorruption.setNodeInstanceName("rdc-distribution");
        Participant bot = new Participant();
        bot.setMail("automatic");
        rdcItemCorruption.setOwner(bot);
        rdcItemCorruption.setIsCorruptionDetected(Boolean.TRUE);
        rdcItemCorruption.setTimestamp(Instant.now());
        Document document = new Document();
        document.setTitle("Corruption detected");
        rdcItemCorruption.setDocument(document);
        return rdcItemCorruption;
    }
}
