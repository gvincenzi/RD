package org.rd.node.binding.message.entity;

import lombok.Data;

@Data
public class ItemProposition {
    Participant owner;
    Document document;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemProposition)) return false;

        ItemProposition that = (ItemProposition) o;

        if (!getOwner().equals(that.getOwner())) return false;
        return getDocument().equals(that.getDocument());
    }

    @Override
    public int hashCode() {
        int result = getOwner().hashCode();
        result = 31 * result + getDocument().hashCode();
        return result;
    }
}
