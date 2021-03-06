package org.rd.distribution.binding.test;

import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rd.distribution.binding.MQListener;
import org.rd.distribution.delivery.service.DistributionConcurrenceService;
import org.rd.distribution.domain.entity.Document;
import org.rd.distribution.domain.entity.Participant;
import org.rd.distribution.domain.entity.RDItem;
import org.rd.distribution.spike.controller.ControllerResponseCache;
import org.rd.distribution.binding.message.DistributionEventType;
import org.rd.distribution.binding.message.DistributionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.MessageChannel;
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
    @Qualifier("distributionChannel")
    MessageChannel distributionChannel;

    @Test
    public void processEntryResponseTest1(){
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_RESPONSE);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        items.add(getRdcItem());
        msg.setContent(items);

        //Correlation ID added by Controller request
        DistributionConcurrenceService.getCorrelationIDs().add(msg.getCorrelationID());

        mqListener.processEntryResponse(msg);

        Assert.assertFalse(DistributionConcurrenceService.getCorrelationIDs().contains(msg.getCorrelationID()));
    }

    @Test
    public void processEntryResponseTest2(){
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        items.add(getRdcItem());
        msg.setContent(items);

        //Correlation ID added by Controller request
        DistributionConcurrenceService.getCorrelationIDs().add(msg.getCorrelationID());

        mqListener.processEntryResponse(msg);

        Assert.assertFalse(DistributionConcurrenceService.getCorrelationIDs().contains(msg.getCorrelationID()));
        Assert.assertNotNull(ControllerResponseCache.getFromCache(msg.getCorrelationID()));

        //Correlation ID remved from cache by Controller request
        Assert.assertNotNull(ControllerResponseCache.removeFromCache(msg.getCorrelationID()));
        Assert.assertNull(ControllerResponseCache.getFromCache(msg.getCorrelationID()));
    }

    @Test
    public void processEntryResponseTest3(){
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.CORRUPTION_DETECTED);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        items.add(getRdcItem());
        msg.setContent(items);

        //Correlation ID added by Controller request
        DistributionConcurrenceService.getCorrelationIDs().add(msg.getCorrelationID());

        mqListener.processEntryResponse(msg);

        RDItem rdItem = msg.getContent().iterator().next();
        Assert.assertTrue(rdItem.getIsCorruptionDetected());
        Assert.assertFalse(DistributionConcurrenceService.getCorrelationIDs().contains(msg.getCorrelationID()));
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
