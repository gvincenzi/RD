package org.rdc.node.binding.test;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.rdc.node.binding.MQListener;
import org.rdc.node.binding.message.DistributionEventType;
import org.rdc.node.binding.message.DistributionMessage;
import org.rdc.node.binding.message.entity.Document;
import org.rdc.node.binding.message.entity.ItemProposition;
import org.rdc.node.binding.message.entity.Participant;
import org.rdc.node.core.entity.RDCItem;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.core.service.RDCItemService;
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
    RDCItemService rdcItemService;

    @MockBean
    @Qualifier("responseChannel")
    MessageChannel responseChannel;

    @Test
    public void processItemPropositionTest(){
        RDCItem rdcItem = getRdcItem();

        DistributionMessage<ItemProposition> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_PROPOSITION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        ItemProposition proposition = new ItemProposition();
        proposition.setDocument(rdcItem.getDocument());
        proposition.setOwner(rdcItem.getOwner());
        msg.setContent(proposition);

        mqListener.processItemProposition(msg);
    }

    @Test
    public void processDistributionTest1() throws RDCNodeException {
        DistributionMessage<List<RDCItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_RESPONSE);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDCItem> items = new ArrayList<>();
        RDCItem rdcItem = getRdcItem();
        items.add(rdcItem);
        msg.setContent(items);

        Mockito.when(rdcItemService.forceAddItem(rdcItem)).thenReturn(Boolean.TRUE);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest1Corrupted() throws RDCNodeException {
        DistributionMessage<List<RDCItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_RESPONSE);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDCItem> items = new ArrayList<>();
        RDCItem rdcItem = getRdcItem();
        items.add(rdcItem);
        msg.setContent(items);

        Mockito.when(rdcItemService.forceAddItem(rdcItem)).thenReturn(Boolean.FALSE);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest2() throws RDCNodeException {
        DistributionMessage<List<RDCItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDCItem> items = new ArrayList<>();
        RDCItem rdcItem = getRdcItem();
        items.add(rdcItem);
        msg.setContent(items);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest2Corrupted() throws RDCNodeException {
        DistributionMessage<List<RDCItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDCItem> items = new ArrayList<>();
        RDCItem rdcItem = getRdcItem();
        items.add(rdcItem);
        msg.setContent(items);

        BDDMockito.willThrow(new RDCNodeException("Test")).given(rdcItemService).init(items);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest3() throws RDCNodeException {
        DistributionMessage<List<RDCItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.CORRUPTION_DETECTED);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDCItem> items = new ArrayList<>();
        RDCItem rdcItem = getRdcItem();
        items.add(rdcItem);
        msg.setContent(items);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest3Corrupted() throws RDCNodeException {
        DistributionMessage<List<RDCItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.CORRUPTION_DETECTED);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDCItem> items = new ArrayList<>();
        RDCItem rdcItem = getRdcItem();
        items.add(rdcItem);
        msg.setContent(items);

        Mockito.when(rdcItemService.forceAddItem(rdcItem)).thenThrow(new RDCNodeException("Test"));
        mqListener.processDistribution(msg);
    }

    @Test
    public void processIntegrityTest4() throws RDCNodeException {
        DistributionMessage<Void> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        
        List<RDCItem> items = new ArrayList<>();
        RDCItem rdcItem = getRdcItem();
        items.add(rdcItem);
        Mockito.when(rdcItemService.findAll()).thenReturn(items);
        Mockito.when(rdcItemService.validate(items)).thenReturn(Boolean.TRUE);
        
        mqListener.processIntegrity(msg);
    }

    @Test
    public void processIntegrityTest4Corrupted() throws RDCNodeException {
        DistributionMessage<Void> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");

        List<RDCItem> items = new ArrayList<>();
        RDCItem rdcItem = getRdcItem();
        items.add(rdcItem);
        Mockito.when(rdcItemService.findAll()).thenReturn(items);
        Mockito.when(rdcItemService.validate(items)).thenReturn(Boolean.FALSE);

        mqListener.processIntegrity(msg);
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
