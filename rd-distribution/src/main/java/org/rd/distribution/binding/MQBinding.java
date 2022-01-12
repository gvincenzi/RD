package org.rd.distribution.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MQBinding {
    @Input("responseChannel")
    SubscribableChannel responseChannel();

    @Output("requestChannel")
    MessageChannel requestChannel();

    @Output("distributionChannel")
    MessageChannel distributionChannel();

    @Output("requestIntegrityChannel")
    MessageChannel requestIntegrityChannel();
}
