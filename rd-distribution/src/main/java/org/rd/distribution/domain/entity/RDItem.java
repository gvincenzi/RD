package org.rd.distribution.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class RDItem implements Comparable<RDItem>{
    String id;
    String previousId;
    Instant timestamp;
    Integer nonce;
    Document document;
    Participant owner;
    String nodeInstanceName;
    Boolean isCorruptionDetected = Boolean.FALSE;

    @Override
    public int compareTo(RDItem arg0) {
        return getTimestamp().compareTo(arg0.getTimestamp());
    }

    public static RDItem getRdItemCorruption() {
        RDItem rdItemCorruption = new RDItem();
        rdItemCorruption.setId("CORRUPTION_DETECTION");
        rdItemCorruption.setNodeInstanceName("rdc-distribution");
        Participant bot = new Participant();
        bot.setMail("automatic");
        rdItemCorruption.setOwner(bot);
        rdItemCorruption.setIsCorruptionDetected(Boolean.TRUE);
        rdItemCorruption.setTimestamp(Instant.now());
        Document document = new Document();
        document.setTitle("Corruption detected");
        rdItemCorruption.setDocument(document);
        return rdItemCorruption;
    }
}
