package org.rdc.scheduler.binding.test;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rdc.scheduler.binding.MQListener;
import org.rdc.scheduler.binding.message.DistributionEventType;
import org.rdc.scheduler.binding.message.DistributionMessage;
import org.rdc.scheduler.domain.entity.Document;
import org.rdc.scheduler.domain.entity.Participant;
import org.rdc.scheduler.domain.entity.RDCItem;
import org.rdc.scheduler.notifier.valence.NotifierValenceService;
import org.rdc.scheduler.spike.client.SpikeClient;
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
public class MQListenerTest {
    @Autowired
    MQListener mqListener;

    @MockBean
    NotifierValenceService notifierValenceService;

    @MockBean
    SpikeClient spikeClient;

    @Test
    public void processEntryResponseTest(){
        DistributionMessage<List<RDCItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_RESPONSE);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDCItem> items = new ArrayList<>();
        items.add(getRdcItem());
        msg.setContent(items);

        mqListener.processEntryResponse(msg);
    }

    private RDCItem getRdcItem() {
        RDCItem item = new RDCItem();
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
