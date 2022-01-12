package org.rd.node.core.service;

import org.rd.node.binding.message.entity.Document;
import org.rd.node.binding.message.entity.Participant;
import org.rd.node.exception.RDNodeException;
import org.rd.node.core.entity.RDItem;

import java.util.List;

public interface RDItemService {
	RDItem add(Document document, Participant owner) throws RDNodeException;
	Boolean forceAddItem(RDItem RDItem) throws RDNodeException;
	Boolean validate(List<RDItem> RDItems) throws RDNodeException;
	List<RDItem> findAll();
	void init(List<RDItem> content) throws RDNodeException;
}
