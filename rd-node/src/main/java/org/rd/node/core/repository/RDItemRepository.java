package org.rd.node.core.repository;

import org.rd.node.core.entity.RDItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RDItemRepository extends MongoRepository<RDItem, String> {
    RDItem findTopByOrderByTimestampDesc();
    List<RDItem> findByIsCorruptionDetectedTrue();
    List<RDItem> findAllByOrderByTimestampAsc();
}
