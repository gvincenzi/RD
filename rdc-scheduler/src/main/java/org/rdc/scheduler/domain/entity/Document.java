package org.rdc.scheduler.domain.entity;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Jacksonized
public class Document {
    String title;
    Map<String, Object> details = new LinkedHashMap<>();

    @JsonAnySetter
    void setDetail(String key, Object value) {
        details.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;

        Document document = (Document) o;

        if (!getTitle().equals(document.getTitle())) return false;
        return getDetails().equals(document.getDetails());
    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + getDetails().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "'" + title + '\'' +
                ", " + details +
                '}';
    }
}
