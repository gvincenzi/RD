package org.rd.scheduler.instruction.test;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rd.scheduler.instruction.ScheduledInstruction;
import org.rd.scheduler.notifier.valence.NotifierValenceService;
import org.rd.scheduler.spike.client.SpikeClient;
import org.rd.scheduler.binding.message.DistributionEventType;
import org.rd.scheduler.binding.message.DistributionMessage;
import org.rd.scheduler.domain.entity.Document;
import org.rd.scheduler.domain.entity.Participant;
import org.rd.scheduler.domain.entity.RDItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ScheduledInstructionTest {
    @MockBean
    SpikeClient spikeClient;

    @Autowired
    ScheduledInstruction scheduledInstruction;

    @MockBean
    NotifierValenceService notifierValenceService;

    @Test
    public void integrityVerificationTest() {
        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        Mockito.when(spikeClient.integrityVerification()).thenReturn(distributionMessage);

        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        items.add(getRdcItem());
        msg.setContent(items);
        msg.setRdValid(Boolean.TRUE);
        Mockito.when(spikeClient.getResult(distributionMessage.getCorrelationID())).thenReturn(msg);

        scheduledInstruction.setInstructionScheduledActive(Boolean.TRUE);
        scheduledInstruction.integrityVerification();
        scheduledInstruction.setInstructionScheduledActive(Boolean.FALSE);
    }

    @Test
    public void integrityVerificationTest2() {
        DistributionMessage<Void> distributionMessage = new DistributionMessage<>();
        distributionMessage.setCorrelationID(UUID.randomUUID());
        distributionMessage.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        Mockito.when(spikeClient.integrityVerification()).thenReturn(distributionMessage);

        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        items.add(getRdcItem());
        msg.setContent(items);
        msg.setRdValid(Boolean.FALSE);
        Mockito.when(spikeClient.getResult(distributionMessage.getCorrelationID())).thenReturn(msg);

        scheduledInstruction.setInstructionScheduledActive(Boolean.TRUE);
        scheduledInstruction.integrityVerification();
        scheduledInstruction.setInstructionScheduledActive(Boolean.FALSE);
    }

    private RDItem getRdcItem() {
        RDItem item = new RDItem();
        item.setIsCorruptionDetected(Boolean.FALSE);
        item.setId(UUID.randomUUID().toString());
        item.setTimestamp(Instant.now());
        item.setNodeInstanceName("test-instance");
        item.setPreviousId(UUID.randomUUID().toString());
        item.setNonce(new Random().nextInt());
        Participant owner = new Participant();
        owner.setMail("test@test.com");
        item.setOwner(owner);
        Document document = new Document();
        document.setTitle("Test document");
        item.setDocument(document);
        return item;
    }
}
