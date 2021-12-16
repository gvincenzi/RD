package org.rdc.node.repository;

import org.rdc.node.domain.entity.RDCItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RDCItemRepository extends MongoRepository<RDCItem, String> {
    RDCItem findTopByOrderByTimestampDesc();
    List<RDCItem> findByIsCorruptionDetectedTrue();
    List<RDCItem> findAllByOrderByTimestampAsc();
}
