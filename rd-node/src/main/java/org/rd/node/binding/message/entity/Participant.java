package org.rd.node.binding.message.entity;

import lombok.Data;

@Data
public class Participant {
    String mail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;

        Participant that = (Participant) o;

        return getMail().equals(that.getMail());
    }

    @Override
    public int hashCode() {
        return getMail().hashCode();
    }
}
