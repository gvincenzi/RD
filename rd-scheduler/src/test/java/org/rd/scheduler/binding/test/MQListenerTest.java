package org.rd.scheduler.binding.test;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rd.scheduler.notifier.valence.NotifierValenceService;
import org.rd.scheduler.spike.client.SpikeClient;
import org.rd.scheduler.binding.MQListener;
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
public class MQListenerTest {
    @Autowired
    MQListener mqListener;

    @MockBean
    NotifierValenceService notifierValenceService;

    @MockBean
    SpikeClient spikeClient;

    @Test
    public void processEntryResponseTest(){
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_RESPONSE);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        items.add(getRdcItem());
        msg.setContent(items);

        mqListener.processEntryResponse(msg);
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
