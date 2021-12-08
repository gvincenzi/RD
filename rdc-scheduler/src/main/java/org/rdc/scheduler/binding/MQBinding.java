package org.rdc.scheduler.binding;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface MQBinding {
    @Output("responseChannel")
    SubscribableChannel responseChannel();
}
