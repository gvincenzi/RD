package org.rd.node.binding.test;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.rd.node.binding.MQListener;
import org.rd.node.binding.message.DistributionEventType;
import org.rd.node.binding.message.DistributionMessage;
import org.rd.node.binding.message.entity.Document;
import org.rd.node.binding.message.entity.ItemProposition;
import org.rd.node.binding.message.entity.Participant;
import org.rd.node.core.entity.RDItem;
import org.rd.node.exception.RDNodeException;
import org.rd.node.core.service.RDItemService;
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
    RDItemService RDItemService;

    @MockBean
    @Qualifier("responseChannel")
    MessageChannel responseChannel;

    @Test
    public void processItemPropositionTest(){
        RDItem RDItem = getRdcItem();

        DistributionMessage<ItemProposition> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_PROPOSITION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        ItemProposition proposition = new ItemProposition();
        proposition.setDocument(RDItem.getDocument());
        proposition.setOwner(RDItem.getOwner());
        msg.setContent(proposition);

        mqListener.processItemProposition(msg);
    }

    @Test
    public void processDistributionTest1() throws RDNodeException {
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_RESPONSE);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        RDItem RDItem = getRdcItem();
        items.add(RDItem);
        msg.setContent(items);

        Mockito.when(RDItemService.forceAddItem(RDItem)).thenReturn(Boolean.TRUE);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest1Corrupted() throws RDNodeException {
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.ENTRY_RESPONSE);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        RDItem RDItem = getRdcItem();
        items.add(RDItem);
        msg.setContent(items);

        Mockito.when(RDItemService.forceAddItem(RDItem)).thenReturn(Boolean.FALSE);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest2() throws RDNodeException {
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        RDItem RDItem = getRdcItem();
        items.add(RDItem);
        msg.setContent(items);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest2Corrupted() throws RDNodeException {
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        RDItem RDItem = getRdcItem();
        items.add(RDItem);
        msg.setContent(items);

        BDDMockito.willThrow(new RDNodeException("Test")).given(RDItemService).init(items);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest3() throws RDNodeException {
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.CORRUPTION_DETECTED);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        RDItem RDItem = getRdcItem();
        items.add(RDItem);
        msg.setContent(items);

        mqListener.processDistribution(msg);
    }

    @Test
    public void processDistributionTest3Corrupted() throws RDNodeException {
        DistributionMessage<List<RDItem>> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.CORRUPTION_DETECTED);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        List<RDItem> items = new ArrayList<>();
        RDItem RDItem = getRdcItem();
        items.add(RDItem);
        msg.setContent(items);

        Mockito.when(RDItemService.forceAddItem(RDItem)).thenThrow(new RDNodeException("Test"));
        mqListener.processDistribution(msg);
    }

    @Test
    public void processIntegrityTest4() throws RDNodeException {
        DistributionMessage<Void> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");
        
        List<RDItem> items = new ArrayList<>();
        RDItem RDItem = getRdcItem();
        items.add(RDItem);
        Mockito.when(RDItemService.findAll()).thenReturn(items);
        Mockito.when(RDItemService.validate(items)).thenReturn(Boolean.TRUE);
        
        mqListener.processIntegrity(msg);
    }

    @Test
    public void processIntegrityTest4Corrupted() throws RDNodeException {
        DistributionMessage<Void> msg = new DistributionMessage<>();
        msg.setType(DistributionEventType.INTEGRITY_VERIFICATION);
        msg.setCorrelationID(UUID.randomUUID());
        msg.setInstanceName("test-instance");

        List<RDItem> items = new ArrayList<>();
        RDItem RDItem = getRdcItem();
        items.add(RDItem);
        Mockito.when(RDItemService.findAll()).thenReturn(items);
        Mockito.when(RDItemService.validate(items)).thenReturn(Boolean.FALSE);

        mqListener.processIntegrity(msg);
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
