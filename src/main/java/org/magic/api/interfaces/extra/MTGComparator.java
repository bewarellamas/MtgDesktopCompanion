package org.magic.api.interfaces.extra;

import java.util.Comparator;

import org.magic.api.beans.MTGCard;

public interface MTGComparator<T extends MTGCard> extends Comparator<MTGCard> {

	public int getWeight(T mc);


}
