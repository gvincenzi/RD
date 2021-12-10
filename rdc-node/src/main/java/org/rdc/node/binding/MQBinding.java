package org.rdc.node.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MQBinding {
    @Input("requestChannel")
    SubscribableChannel requestChannel();

    @Output("responseChannel")
    MessageChannel responseChannel();
}
