package org.rdc.node.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rdc.node.binding.message.entity.Participant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

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
    private Set<Participant> validators;

    @Transient
    private Boolean validated;

    public RDCItem(org.rdc.node.binding.message.entity.Document document, String previousId, Participant owner) {
        this.setOwner(owner);
        this.setPreviousId(previousId);
        this.setDocument(document);
        this.setTimestamp(Instant.now());
    }

    @Override
    public int compareTo(RDCItem arg0) {
        return getTimestamp().compareTo(arg0.getTimestamp());
    }
}
