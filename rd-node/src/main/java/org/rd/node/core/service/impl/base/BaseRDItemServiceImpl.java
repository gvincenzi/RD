package org.rd.node.core.service.impl.base;

import org.rd.node.core.service.NodeUtils;
import org.rd.node.core.service.impl.RDItemServiceImpl;
import org.rd.node.exception.RDNodeException;
import org.rd.node.core.entity.RDItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseRDItemServiceImpl extends RDItemServiceImpl {
	private static final String REGEX_DIGIT = "[0-9].*";

	@Override
	public boolean isHashResolved(RDItem RDItem, Integer difficultLevel) {
		List<Integer> digits = new ArrayList<>(difficultLevel);

		Integer index = 0;
		String hash = RDItem.getId();
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
	public String calculateHash(RDItem RDItem) throws RDNodeException {
		return NodeUtils.applySha256(
				RDItem.getPreviousId() +
						RDItem.getTimestamp().toEpochMilli() +
						RDItem.getNonce() +
						RDItem.getDocument().getTitle() +
						RDItem.getOwner().getMail()
		);
	}
}
