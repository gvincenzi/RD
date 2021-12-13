package org.rdc.node.service.test;

import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rdc.node.RDCNodeApplication;
import org.rdc.node.binding.message.entity.Document;
import org.rdc.node.binding.message.entity.Participant;
import org.rdc.node.domain.entity.RDCItem;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.repository.RDCItemRepository;
import org.rdc.node.service.RDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RDCNodeApplication.class })
@ActiveProfiles("test")
public class RDCItemServiceImplTest {
	@Autowired
	RDCItemService serviceUnderTest;
	
	@MockBean
	RDCItemRepository rdcItemRepository;
	
	@Before
	public void init(){
		Mockito.when(rdcItemRepository.findAllByOrderByTimestampAsc()).thenReturn(new ArrayList<>());
		Mockito.when(rdcItemRepository.save(Mockito.any(RDCItem.class))).then(i -> i.getArgument(0));
	}
	
	@Test(expected = RDCNodeException.class)
	public void testException() throws RDCNodeException{
		serviceUnderTest.add(null, getUserMock());
	}
	
	@Test(expected = RDCNodeException.class)
	public void testMinerException() throws RDCNodeException{
		serviceUnderTest.add(getDocumentMock("Test Document"), null);
	}
	
	@Test
	public void testOK() throws RDCNodeException {
		List<RDCItem> items = new ArrayList<>();
		
		long now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDCItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);
		
		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDCItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);
		
		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDCItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);
		
		items.add(item);
		items.add(item1);
		items.add(item2);

		Assert.assertTrue(serviceUnderTest.validate(items));
	}

	@Test
	public void testKO() throws RDCNodeException {
		List<RDCItem> items = new ArrayList<>();

		long now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDCItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);

		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDCItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);

		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDCItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);

		item2.setTimestamp(Instant.now());

		items.add(item);
		items.add(item1);
		items.add(item2);

		Assert.assertFalse(serviceUnderTest.validate(items));
	}

	@Test
	public void testKO2() throws RDCNodeException {
		List<RDCItem> items = new ArrayList<>();

		long now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDCItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);

		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDCItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);

		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDCItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);

		item2.setPreviousId(UUID.randomUUID().toString());

		items.add(item);
		items.add(item1);
		items.add(item2);

		Assert.assertFalse(serviceUnderTest.validate(items));
	}

	@Test
	public void testKO3() throws RDCNodeException {
		List<RDCItem> items = new ArrayList<>();

		long now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDCItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);

		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDCItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);

		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDCItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);

		item2.setPreviousId(UUID.randomUUID().toString());

		items.add(item);
		items.add(item1);
		items.add(item2);

		Assert.assertFalse(serviceUnderTest.validate(items));
	}

	@Test
	public void testInit() throws RDCNodeException {
		List<RDCItem> items = new ArrayList<>();

		long now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(null);
		RDCItem item = serviceUnderTest.add(getDocumentMock("Genesis block"), getUserMock());
		log.info("Block mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item);

		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item);
		RDCItem item1 = serviceUnderTest.add(getDocumentMock("Item 1"), getUserMock());
		log.info("Item added in "+ (System.currentTimeMillis()-now) + "ms >> " + item1);

		now = System.currentTimeMillis();
		Mockito.when(rdcItemRepository.findTopByOrderByTimestampDesc()).thenReturn(item1);
		RDCItem item2 = serviceUnderTest.add(getDocumentMock("Item 2"), getUserMock());
		log.info("Item mined in "+ (System.currentTimeMillis()-now) + "ms >> " + item2);

		items.add(item);
		items.add(item1);
		items.add(item2);

		serviceUnderTest.init(items);
	}

	@Test
	public void calculateHashTest() throws Exception{
		RDCItem item = new RDCItem();
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
