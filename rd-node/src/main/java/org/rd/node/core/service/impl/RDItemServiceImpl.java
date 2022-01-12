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

    public abstract boolean isHashResolved(RDItem RDItem, Integer difficultLevel);

    public abstract String calculateHash(RDItem RDItem) throws RDNodeException;

    protected RDItem getNewItem(Document document, RDItem previous, Participant owner) throws RDNodeException {
        if (document == null || owner == null) {
            throw new RDNodeException("Document and Owner are mandatory");
        }
        RDItem RDItem = new RDItem(document, previous != null ? previous.getId() : GENESIS, owner, instanceName);

        Random random = new Random(RDItem.getTimestamp().toEpochMilli());
        int nonce = random.nextInt();
        RDItem.setNonce(nonce);
        RDItem.setId(calculateHash(RDItem));
        while (!isHashResolved(RDItem, difficultLevel)) {
            nonce = random.nextInt();
            RDItem.setNonce(nonce);
            RDItem.setId(calculateHash(RDItem));
        }

        RDItem.setOwner(owner);
        return RDItem;
    }

    @Override
    public Boolean forceAddItem(RDItem RDItem) throws RDNodeException {
        if (RDItemRepository.findByIsCorruptionDetectedTrue().size() == 0 && !RDItemRepository.existsById(RDItem.getId())) {
                RDItemRepository.save(RDItem);
                return validate(RDItemRepository.findAllByOrderByTimestampAsc());
        }

        return Boolean.TRUE;
    }

    @Override
    public List<RDItem> findAll() {
        return RDItemRepository.findAllByOrderByTimestampAsc();
    }

    @Override
    public Boolean validate(List<RDItem> RDItems) throws RDNodeException {
        if (RDItems == null) {
            throw new RDNodeException("Iterable items collection is mandatory");
        }
        RDItem currentItem;
        RDItem previousItem;

        Collections.sort(RDItems);

        Boolean result = true;
        for (int i = 0; i < RDItems.size(); i++) {
            previousItem = i > 0 ? RDItems.get(i - 1) : null;
            currentItem = RDItems.get(i);
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
