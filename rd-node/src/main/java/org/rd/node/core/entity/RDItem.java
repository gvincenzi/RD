package org.rd.node.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rd.node.binding.message.entity.Participant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@Document("rd_item")
public class RDItem implements Comparable<RDItem>{
    @Id
    private String id;
    private String previousId;
    private Instant timestamp;
    private Integer nonce;
    private org.rd.node.binding.message.entity.Document document;
    private Participant owner;
    private String nodeInstanceName;
    private Boolean isCorruptionDetected = Boolean.FALSE;

    public RDItem(org.rd.node.binding.message.entity.Document document, String previousId, Participant owner, String nodeInstanceName) {
        this.setOwner(owner);
        this.setPreviousId(previousId);
        this.setDocument(document);
        this.setTimestamp(Instant.now());
        this.setNodeInstanceName(nodeInstanceName);
    }

    @Override
    public int compareTo(RDItem arg0) {
        return getTimestamp().compareTo(arg0.getTimestamp());
    }
}
