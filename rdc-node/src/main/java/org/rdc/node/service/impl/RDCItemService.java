package org.rdc.node.service.impl;

import lombok.extern.java.Log;
import org.rdc.node.binding.message.entity.Document;
import org.rdc.node.binding.message.entity.Participant;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.domain.entity.RDCItem;
import org.rdc.node.repository.RDCItemRepository;
import org.rdc.node.service.IRDCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Log
public abstract class RDCItemService implements IRDCItemService {
	private static final String GENESIS = "GENESIS";

	@Autowired
	private RDCItemRepository rdcItemRepository;

	@Value("${difficult.level}")
	private Integer difficultLevel;
	
	@Value("${required.validation.number}")
	private Integer requiredValidationNumber;

	@Value("${spring.application.name}")
	private String instanceName;

	public abstract boolean isHashResolved(RDCItem rdcItem, Integer difficultLevel);
	public abstract String calculateHash(RDCItem rdcItem) throws RDCNodeException;
	
	protected RDCItem getNewItem(Document document, RDCItem previous, Participant owner) throws RDCNodeException {
		if (document == null || owner == null) {
			throw new RDCNodeException("Document and Owner are mandatory");
		}
		RDCItem rdcItem = new RDCItem(document, previous != null ? previous.getId() : GENESIS, owner, instanceName);
		
		Random random = new Random(rdcItem.getTimestamp().toEpochMilli());
		int nonce = random.nextInt();
		rdcItem.setNonce(nonce);
		rdcItem.setId(calculateHash(rdcItem));
		while (!isHashResolved(rdcItem, difficultLevel)) {
			nonce = Math.abs(random.nextInt());
			rdcItem.setNonce(nonce);
			rdcItem.setId(calculateHash(rdcItem));
		}

		rdcItem.setOwner(owner);
		return rdcItem;
	}

	@Override
	public Boolean forceAddItem(RDCItem rdcItem) throws RDCNodeException {
		rdcItemRepository.save(rdcItem);
		return validate(rdcItemRepository.findAll());
	}

	@Override
	public Boolean validate(Iterable<RDCItem> items) throws RDCNodeException {
		if (items == null) {
			throw new RDCNodeException("Iterable items collection is mandatory");
		}
		RDCItem currentItem;
		RDCItem previousItem;

		List<RDCItem> rdcItems = new ArrayList<>();

		rdcItems.forEach(item -> rdcItems.add(item));
		Collections.sort(rdcItems);

		Boolean result = true;
		for (int i = 0; i < rdcItems.size(); i++) {
			previousItem = i>0?rdcItems.get(i - 1):null;
			currentItem = rdcItems.get(i);
			if (!currentItem.getId().equals(calculateHash(currentItem))) {
				result = false;
			}
			if (previousItem!=null && !previousItem.getId().equals(currentItem.getPreviousId())) {
				result = false;
			}
			if (!isHashResolved(currentItem, difficultLevel)) {
				result = false;
			}
			
			if(i==rdcItems.size()-1 && currentItem.getValidators().size()<requiredValidationNumber && !currentItem.getValidators().contains(instanceName)){
				currentItem.getValidators().add(instanceName);
				rdcItemRepository.save(currentItem);
			}
		}

		return result;
	}

	@Override
	public RDCItem add(Document document, Participant owner) throws RDCNodeException {
		if (document == null) {
			throw new RDCNodeException("Document is mandatory");
		}
		if (owner == null) {
			throw new RDCNodeException("Owner is mandatory");
		}
		RDCItem previous = rdcItemRepository.findTopByOrderByTimestampDesc();
		RDCItem newItem = getNewItem(document, previous, owner);
		return rdcItemRepository.save(newItem);
	}
}
