package org.rdc.node.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rdc.node.binding.message.entity.Participant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@Document("rdc_item")
public class RDCItem implements Comparable<RDCItem>{
    @Id
    private String id;
    private String previousId;
    private Instant timestamp;
    private Integer nonce;
    private org.rdc.node.binding.message.entity.Document document;
    private Participant owner;
    private String nodeInstanceName;

    public RDCItem(org.rdc.node.binding.message.entity.Document document, String previousId, Participant owner, String nodeInstanceName) {
        this.setOwner(owner);
        this.setPreviousId(previousId);
        this.setDocument(document);
        this.setTimestamp(Instant.now());
        this.setNodeInstanceName(nodeInstanceName);
    }

    @Override
    public int compareTo(RDCItem arg0) {
        return getTimestamp().compareTo(arg0.getTimestamp());
    }
}
