package org.rdc.node.binding.message.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class Entry {
    Participant owner;
    Document document;
    String id;
    Instant insertionInstant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;

        Entry entry = (Entry) o;

        if (!getOwner().equals(entry.getOwner())) return false;
        if (!getDocument().equals(entry.getDocument())) return false;
        if (!getId().equals(entry.getId())) return false;
        return getInsertionInstant().equals(entry.getInsertionInstant());
    }

    @Override
    public int hashCode() {
        int result = getOwner().hashCode();
        result = 31 * result + getDocument().hashCode();
        result = 31 * result + getId().hashCode();
        result = 31 * result + getInsertionInstant().hashCode();
        return result;
    }
}
