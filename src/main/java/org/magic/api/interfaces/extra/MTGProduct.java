package org.magic.api.interfaces.extra;

import org.magic.api.beans.MTGEdition;
import org.magic.api.beans.enums.EnumItems;
import org.magic.api.beans.shop.Category;

public interface MTGProduct extends MTGSerializable {

	public Long getProductId();

	public void setProductId(Long id);

	public String getUrl();

	public void setUrl(String url);

	public MTGEdition getEdition();

	public void setEdition(MTGEdition edition);

	public String getName();

	public void setName(String name);

	public void setTypeProduct(EnumItems type);

	public EnumItems getTypeProduct();

	public void setCategory(Category c);

	public Category getCategory();

	default boolean isSealed()
	{
		return getTypeProduct()!=EnumItems.CARD;
	}



}