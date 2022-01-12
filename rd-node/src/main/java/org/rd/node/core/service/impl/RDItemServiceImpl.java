package org.rd.node.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.rd.node.core.repository.RDItemRepository;
import org.rd.node.binding.message.entity.Document;
import org.rd.node.binding.message.entity.Participant;
import org.rd.node.core.entity.RDItem;
import org.rd.node.core.service.RDItemService;
import org.rd.node.exception.RDNodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Log
public abstract class RDItemServiceImpl implements RDItemService {
    private static final String GENESIS = "GENESIS";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private RDItemRepository RDItemRepository;

    @Value("${rd.difficult.level}")
    private Integer difficultLevel;

    @Value("${spring.application.name}")
    private String instanceName;

    public abstract boolean isHashResolved(RDItem rdItem, Integer difficultLevel);

    public abstract String calculateHash(RDItem rdItem) throws RDNodeException;

    protected RDItem getNewItem(Document document, RDItem previous, Participant owner) throws RDNodeException {
        if (document == null || owner == null) {
            throw new RDNodeException("Document and Owner are mandatory");
        }
        RDItem rdItem = new RDItem(document, previous != null ? previous.getId() : GENESIS, owner, instanceName);

        Random random = new Random(rdItem.getTimestamp().toEpochMilli());
        int nonce = random.nextInt();
        rdItem.setNonce(nonce);
        rdItem.setId(calculateHash(rdItem));
        while (!isHashResolved(rdItem, difficultLevel)) {
            nonce = random.nextInt();
            rdItem.setNonce(nonce);
            rdItem.setId(calculateHash(rdItem));
        }

        rdItem.setOwner(owner);
        return rdItem;
    }

    @Override
    public Boolean forceAddItem(RDItem rdItem) throws RDNodeException {
        if (RDItemRepository.findByIsCorruptionDetectedTrue().size() == 0 && !RDItemRepository.existsById(rdItem.getId())) {
                RDItemRepository.save(rdItem);
                return validate(RDItemRepository.findAllByOrderByTimestampAsc());
        }

        return Boolean.TRUE;
    }

    @Override
    public List<RDItem> findAll() {
        return RDItemRepository.findAllByOrderByTimestampAsc();
    }

    @Override
    public Boolean validate(List<RDItem> rdItems) throws RDNodeException {
        if (rdItems == null) {
            throw new RDNodeException("Iterable items collection is mandatory");
        }
        RDItem currentItem;
        RDItem previousItem;

        Collections.sort(rdItems);

        Boolean result = true;
        for (int i = 0; i < rdItems.size(); i++) {
            previousItem = i > 0 ? rdItems.get(i - 1) : null;
            currentItem = rdItems.get(i);
            if (!currentItem.getId().equals(calculateHash(currentItem))) {
                result = false;
            }
            if (previousItem != null && !previousItem.getId().equals(currentItem.getPreviousId())) {
                result = false;
            }
            if (previousItem == null && !GENESIS.equals(currentItem.getPreviousId())) {
                result = false;
            }
            if (!isHashResolved(currentItem, difficultLevel)) {
                result = false;
            }
        }

        return result;
    }

    @Override
    public RDItem add(Document document, Participant owner) throws RDNodeException {
        if (document == null) {
            throw new RDNodeException("Document is mandatory");
        }
        if (owner == null) {
            throw new RDNodeException("Owner is mandatory");
        }
        if (RDItemRepository.findByIsCorruptionDetectedTrue().size() > 0) {
            throw new RDNodeException("RD is corrupted");
        }
        RDItem previous = RDItemRepository.findTopByOrderByTimestampDesc();
        RDItem newItem = getNewItem(document, previous, owner);
        return RDItemRepository.save(newItem);
    }

    @Override
    public void init(List<RDItem> content) throws RDNodeException {
        Collections.sort(content);
        List<RDItem> RDItems = RDItemRepository.findAll(Sort.by("timestamp"));
        for (int i = 0; i < content.size(); i++) {
            if (i < RDItems.size()) {
                if (!RDItems.get(i).equals(content.get(i))) {
                    throw new RDNodeException("RD has been corrupted");
                }
            } else {
                forceAddItem(content.get(i));
            }
        }
    }
}
