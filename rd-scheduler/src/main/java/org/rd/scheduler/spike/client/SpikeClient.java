package org.rd.scheduler.spike.client;

import org.rd.scheduler.binding.message.DistributionMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "spike", url = "${rd.spike.url}")
public interface SpikeClient {
    @GetMapping("/{correlationID}")
    DistributionMessage getResult(@PathVariable UUID correlationID);

    @PostMapping("/verify")
    DistributionMessage<Void> integrityVerification();
}
