package org.rdd.distribution.binding;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MQBinding {
    @Output("requestChannel")
    MessageChannel requestChannel();
}
