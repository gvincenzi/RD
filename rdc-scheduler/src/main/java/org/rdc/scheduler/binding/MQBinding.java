package org.rdc.scheduler.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface MQBinding {
    @Input("responseChannel")
    SubscribableChannel responseChannel();
}
