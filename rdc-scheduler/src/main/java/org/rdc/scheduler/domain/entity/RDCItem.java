package org.rdc.scheduler.domain.entity;

import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class RDCItem {
    private String id;
    private String previousId;
    private Instant timestamp;
    private Integer nonce;
    private Document document;
    private Participant owner;
    private Set<String> validators;
    private String nodeIstanceName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RDCItem)) return false;

        RDCItem RDCItem = (RDCItem) o;

        if (!getOwner().equals(RDCItem.getOwner())) return false;
        if (!getDocument().equals(RDCItem.getDocument())) return false;
        if (!getId().equals(RDCItem.getId())) return false;
        return getTimestamp().equals(RDCItem.getTimestamp());
    }

    @Override
    public int hashCode() {
        int result = getOwner().hashCode();
        result = 31 * result + getDocument().hashCode();
        result = 31 * result + getId().hashCode();
        result = 31 * result + getTimestamp().hashCode();
        return result;
    }
}
