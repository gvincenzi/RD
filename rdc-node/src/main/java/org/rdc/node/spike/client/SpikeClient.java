package org.rdc.node.spike.client;

import org.rdc.node.binding.message.DistributionMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "spike", url = "${rdc.spike.url}")
public interface SpikeClient {
    @PostMapping("/verify/internal")
    DistributionMessage<Void> integrityVerification();
}
