package org.rdc.distribution.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
public class RDCItem implements Comparable<RDCItem>{
    private String id;
    private String previousId;
    private Instant timestamp;
    private Integer nonce;
    private Document document;
    private Participant owner;
    private Set<String> validators;
    private String nodeIstanceName;

    @Transient
    private Boolean validated;

    public RDCItem(Document document, String previousId, Participant owner, String nodeIstanceName) {
        this.setOwner(owner);
        this.setPreviousId(previousId);
        this.setDocument(document);
        this.setTimestamp(Instant.now());
        this.setNodeIstanceName(nodeIstanceName);
    }

    @Override
    public int compareTo(RDCItem arg0) {
        return getTimestamp().compareTo(arg0.getTimestamp());
    }
}
