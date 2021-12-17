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
    public String toString() {
        return "{" +
                "'" + title + '\'' +
                ", " + details +
                '}';
    }
}
