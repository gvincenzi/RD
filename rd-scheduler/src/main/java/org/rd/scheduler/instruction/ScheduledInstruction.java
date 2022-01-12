package org.rd.scheduler.instruction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.java.Log;
import org.rd.scheduler.notifier.valence.NotifierValenceService;
import org.rd.scheduler.binding.message.DistributionMessage;
import org.rd.scheduler.domain.entity.Participant;
import org.rd.scheduler.domain.entity.RDItem;
import org.rd.scheduler.spike.client.SpikeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log
@Data
@Configuration
public class ScheduledInstruction {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SpikeClient spikeClient;

    @Autowired
    NotifierValenceService notifierValenceService;

    @Value("${rd.instructionScheduled}")
    Boolean instructionScheduledActive;

    @Scheduled(fixedDelay = 60000)
    public void integrityVerification() {
        if(instructionScheduledActive) {
            log.info("START >> Scheduled Instruction - IntegrityVerification");
            DistributionMessage<Void> integrityVerification = spikeClient.integrityVerification();
            DistributionMessage<List<RDItem>> integrityVerificationResponse = spikeClient.getResult(integrityVerification.getCorrelationID());
            while (integrityVerificationResponse == null || integrityVerificationResponse.getContent() == null) {
                integrityVerificationResponse = spikeClient.getResult(integrityVerification.getCorrelationID());
            }

            if (Boolean.FALSE.equals(integrityVerificationResponse.getRdValid())) {
                log.info("ALERT >> Scheduled Instruction - IntegrityVerification : Corruption detected");
                List<RDItem> items = objectMapper.convertValue(integrityVerificationResponse.getContent(), new TypeReference<>() {
                });
                Set<Participant> participants = new HashSet<>();
                for (RDItem rdcItem : items) {
                    if (rdcItem.getOwner() != null) {
                        participants.add(rdcItem.getOwner());
                    }
                }
                notifierValenceService.sendCorruptionMail(participants);
            }
            log.info("END >> Scheduled Instruction - IntegrityVerification");
        }
    }
}
