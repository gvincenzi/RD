package org.rdd.distribution.binding.message;

import lombok.Data;
import org.rdd.distribution.domain.entity.EntryProposition;

@Data
public class DistributionMessage {
    private EntryProposition entryProposition;
}
