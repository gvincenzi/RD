package org.rdc.node.service.impl.base;

import org.rdc.node.exception.RDCNodeException;
import org.rdc.node.domain.entity.RDCItem;
import org.rdc.node.service.impl.RDCItemService;
import org.rdc.node.util.NodeUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseRDCItemService extends RDCItemService {
	private static final String REGEX_DIGIT = "[0-9].*";

	@Override
	public boolean isHashResolved(RDCItem rdcItem, Integer difficultLevel) {
		List<Integer> digits = new ArrayList<>(difficultLevel);

		Integer index = 0;
		String hash = rdcItem.getId();
		while (index < hash.length() && digits.size() < difficultLevel) {
			String s = hash.substring(index, ++index);
			if (s.matches(REGEX_DIGIT)) {
				digits.add(Integer.parseInt(s));
			}
		}

		Integer sum = digits.parallelStream().reduce(0, Integer::sum);
		return sum % difficultLevel == 0;
	}

	@Override
	public String calculateHash(RDCItem rdcItem) throws RDCNodeException {
		return NodeUtils.applySha256(
				rdcItem.getPreviousId() +
						rdcItem.getTimestamp().toEpochMilli() +
						rdcItem.getNonce() +
						rdcItem.getDocument().getTitle() +
						rdcItem.getOwner().getMail()
		);
	}
}
