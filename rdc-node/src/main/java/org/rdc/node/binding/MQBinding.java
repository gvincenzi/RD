package org.rdc.node.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MQBinding {
    @Input("requestChannel")
    SubscribableChannel requestChannel();
}
