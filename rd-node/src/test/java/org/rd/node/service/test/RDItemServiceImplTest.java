package org.rd.node.service.test;

import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rd.node.core.repository.RDItemRepository;
import org.rd.node.RDNodeApplication;
import org.rd.node.binding.message.entity.Document;
import org.rd.node.binding.message.entity.Participant;
import org.rd.node.spike.client.SpikeClient;
import org.rd.node.core.entity.RDItem;
import org.rd.node.exception.RDNodeException;
import org.rd.node.core.service.RDItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RDNodeApplication.class })
@ActiveProfiles("test")
public class RDItemServiceImplTest {
	@Autowired
	RDItemService serviceUnderTest;
	
	@MockBean
	RDItemRepository RDItemRepository;

	@MockBean
	SpikeClient spikeClient;
	
	@Before
	public void init(){
		Mockito.when(RDItemRepository.findAllByOrderByTimestampAsc()).thenReturn(new ArrayList<>());
		Mockito.when(RDItemRepository.save(Mockito.any(RDItem.class))).then(i -> i.getArgument(0));
	}
	
	@Test(expected = RDNodeException.class)
	public void testException() throws RDNodeException {
		serviceUnderTest.add(null, getUserMock());
	}
	
	@Test(expected = RDNodeException.class)
	public void testMinerException() throws RDNodeException {
		serviceUnderTest.add(getDocumentMock("Test Document"), null);
	}
	
	@Test
	public void testOK() throws RDNodeException {
		List<RDItem> items = new ArrayList<>();
		
		long now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);
		
		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);
		
		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);
		
		items.add(item);
		items.add(item1);
		items.add(item2);

		Assert.assertTrue(serviceUnderTest.validate(items));
	}

	@Test
	public void testKO() throws RDNodeException {
		List<RDItem> items = new ArrayList<>();

		long now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);

		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);

		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);

		item2.setTimestamp(item2.getTimestamp().plus(Period.ofDays(3)));

		items.add(item);
		items.add(item1);
		items.add(item2);

		Assert.assertFalse(serviceUnderTest.validate(items));
	}

	@Test
	public void testKO2() throws RDNodeException {
		List<RDItem> items = new ArrayList<>();

		long now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);

		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);

		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);

		item2.setPreviousId(UUID.randomUUID().toString());

		items.add(item);
		items.add(item1);
		items.add(item2);

		Assert.assertFalse(serviceUnderTest.validate(items));
	}

	@Test
	public void testKO3() throws RDNodeException {
		List<RDItem> items = new ArrayList<>();

		long now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);

		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);

		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);

		item2.setPreviousId(UUID.randomUUID().toString());

		items.add(item);
		items.add(item1);
		items.add(item2);

		Assert.assertFalse(serviceUnderTest.validate(items));
	}

	@Test
	public void testInit() throws RDNodeException {
		List<RDItem> items = new ArrayList<>();

		long now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);

		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);

		now = System.currentTimeMillis();
		Mockito.when(RDItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);

		items.add(item);
		items.add(item1);
		items.add(item2);

		serviceUnderTest.init(items);
	}

	@Test
	public void calculateHashTest() throws Exception{
		RDItem item = new RDItem();
		serviceUnderTest.forceAddItem(item);
	}

	public Document getDocumentMock(String title){
		Document document = new Document();
		document.setTitle(title);
		return document;
	}
	
	private Participant getUserMock(){
		Participant user = new Participant();
		user.setMail("test@test.com");
		return user;
	}
}
