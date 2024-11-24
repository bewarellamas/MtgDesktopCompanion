package org.magic.api.beans.messages;

import org.magic.api.beans.abstracts.AbstractMessage;
import org.magic.api.interfaces.extra.MTGProduct;

public class SearchMessage extends AbstractMessage {

	private static final long serialVersionUID = 1L;

	private MTGProduct item;
	
	public SearchMessage(MTGProduct item) {
		this.item=item;
		setTypeMessage(MSG_TYPE.SEARCH);
		setMessage("I'm searching  "+ item);
	}

	public MTGProduct getItem() {
		return item;
	}

}
