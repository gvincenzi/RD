package org.rdc.node.repository;

import org.rdc.node.item.RDCItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RDCItemRepository extends MongoRepository<RDCItem, String> {
    RDCItem findTopByOrderByTimestampDesc();
}
