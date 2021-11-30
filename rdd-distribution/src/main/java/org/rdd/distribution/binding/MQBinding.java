package org.rdd.distribution.binding;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MQBinding {
    String ENTRY_PROPOSITION_CHANNEL = "entryPropositionChannel";

    @Output(ENTRY_PROPOSITION_CHANNEL)
    MessageChannel entryPropositionChannel();
}
