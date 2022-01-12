package org.rd.node.spike.client;

import org.rd.node.binding.message.DistributionMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "spike", url = "${rd.spike.url}")
public interface SpikeClient {
    @PostMapping("/verify/internal")
    DistributionMessage<Void> integrityVerification();
}
