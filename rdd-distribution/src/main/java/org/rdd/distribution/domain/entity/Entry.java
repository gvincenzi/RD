package org.rdd.distribution.domain.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class Entry extends EntryProposition {
    Long id;
    Instant insertionDate;
}
