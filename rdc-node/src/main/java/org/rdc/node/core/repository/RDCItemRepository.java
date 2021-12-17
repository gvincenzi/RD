package org.rdc.node.core.repository;

import org.rdc.node.core.entity.RDCItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RDCItemRepository extends MongoRepository<RDCItem, String> {
    RDCItem findTopByOrderByTimestampDesc();
    List<RDCItem> findByIsCorruptionDetectedTrue();
    List<RDCItem> findAllByOrderByTimestampAsc();
}
