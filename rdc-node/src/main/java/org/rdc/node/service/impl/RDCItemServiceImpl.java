package org.rdc.node.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.rdc.node.binding.message.DistributionMessage;
import org.rdc.node.binding.message.entity.Document;
import org.rdc.node.binding.message.entity.Participant;
import org.rdc.node.client.SpikeClient;
import org.rdc.node.domain.entity.RDCItem;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.repository.RDCItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Log
public abstract class RDCItemServiceImpl implements org.rdc.node.service.RDCItemService {
    private static final String GENESIS = "GENESIS";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private RDCItemRepository rdcItemRepository;

	@Autowired
	SpikeClient spikeClient;

    @Value("${required.difficult.level}")
    private Integer difficultLevel;

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
            nonce = random.nextInt();
            rdcItem.setNonce(nonce);
            rdcItem.setId(calculateHash(rdcItem));
        }

        rdcItem.setOwner(owner);
        return rdcItem;
    }

    @Override
    public Boolean forceAddItem(RDCItem rdcItem) throws RDCNodeException {
        rdcItemRepository.save(rdcItem);
        return validate(rdcItemRepository.findAllByOrderByTimestampAsc());
    }

    @Override
    public List<RDCItem> findAll() {
        return rdcItemRepository.findAllByOrderByTimestampAsc();
    }

    @Override
    public Boolean validate(List<RDCItem> rdcItems) throws RDCNodeException {
        if (rdcItems == null) {
            throw new RDCNodeException("Iterable items collection is mandatory");
        }
        RDCItem currentItem;
        RDCItem previousItem;

        Collections.sort(rdcItems);

        Boolean result = true;
        for (int i = 0; i < rdcItems.size(); i++) {
            previousItem = i > 0 ? rdcItems.get(i - 1) : null;
            currentItem = rdcItems.get(i);
            if (!currentItem.getId().equals(calculateHash(currentItem))) {
                result = false;
            }
            if (previousItem != null && !previousItem.getId().equals(currentItem.getPreviousId())) {
                result = false;
            }
            if (!isHashResolved(currentItem, difficultLevel)) {
                result = false;
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

    @Override
    public void init(List<RDCItem> content) throws RDCNodeException {
        Collections.sort(content);
        List<RDCItem> rdcItems = rdcItemRepository.findAll(Sort.by("timestamp"));
        for (int i = 0; i < content.size(); i++) {
            if (i < rdcItems.size()) {
                if (!rdcItems.get(i).equals(content.get(i))) {
                    throw new RDCNodeException("RDC has been corrupted");
                }
            } else {
                forceAddItem(content.get(i));
            }
        }
    }

    @Override
    public void startup() throws RDCNodeException {
        DistributionMessage<Void> integrityVerification = spikeClient.integrityVerification();
        DistributionMessage<List<RDCItem>> integrityVerificationResponse = spikeClient.getResult(integrityVerification.getCorrelationID());
        while (integrityVerificationResponse == null || integrityVerificationResponse.getContent() == null) {
            integrityVerificationResponse = spikeClient.getResult(integrityVerification.getCorrelationID());
        }
        List<RDCItem> items = objectMapper.convertValue(integrityVerificationResponse.getContent(), new TypeReference<List<RDCItem>>() { });
        init(items);
        log.info("RDC correctly started");
    }
}
