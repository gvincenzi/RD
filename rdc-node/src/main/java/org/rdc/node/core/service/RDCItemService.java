package org.rdc.node.core.service;

import org.rdc.node.binding.message.entity.Document;
import org.rdc.node.binding.message.entity.Participant;
import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.core.entity.RDCItem;

import java.util.List;

public interface RDCItemService {
	RDCItem add(Document document, Participant owner) throws RDCNodeException;
	Boolean forceAddItem(RDCItem rdcItem) throws RDCNodeException;
	Boolean validate(List<RDCItem> rdcItems) throws RDCNodeException;
	List<RDCItem> findAll();
	void init(List<RDCItem> content) throws RDCNodeException;
}
