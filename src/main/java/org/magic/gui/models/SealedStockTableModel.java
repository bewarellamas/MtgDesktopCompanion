package org.magic.gui.models;

import org.magic.api.beans.MagicCollection;
import org.magic.api.beans.MagicEdition;
import org.magic.api.beans.Packaging;
import org.magic.api.beans.SealedStock;
import org.magic.api.beans.enums.EnumCondition;
import org.magic.api.beans.enums.EnumItems;
import org.magic.gui.abstracts.GenericTableModel;
import org.magic.tools.UITools;

public class SealedStockTableModel extends GenericTableModel<SealedStock> {

	
	private static final long serialVersionUID = 1L;

	public SealedStockTableModel() {
		setColumns("ID","Type","Edition","LANGUAGE","Quality","Qty","Collection","Price");
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:return Packaging.class;
		case 1: return EnumItems.class;
		case 2: return MagicEdition.class;
		case 3: return String.class;
		case 4: return EnumCondition.class;
		case 5: return Integer.class;
		case 6: return MagicCollection.class;
		case 7: return Double.class;
		default: return super.getColumnClass(columnIndex);
		}
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		SealedStock it = items.get(row);
		switch(column)
		{
			case 0: return it;
			case 1: return it.getProduct().getType();
			case 2: return it.getProduct().getEdition();
			case 3: return it.getProduct().getLang();
			case 4: return it.getCondition();
			case 5 : return it.getQte();
			case 6 : return it.getMagicCollection();
			case 7 : return it.getPrice();
			default : return super.getValueAt(row, column);
		}
	}
	
	
	@Override
	public void setValueAt(Object aValue, int row, int column) {
		
		SealedStock it = items.get(row);
		
		switch(column)
		{
			case 3: it.getProduct().setLang(String.valueOf(aValue));break;
			case 4: it.setCondition(EnumCondition.valueOf(aValue.toString()));break;
			case 5: it.setQte(Integer.parseInt(String.valueOf(aValue)));break;
			case 6: it.setMagicCollection(new MagicCollection(String.valueOf(aValue)));break;
			case 7: it.setPrice(UITools.parseDouble(aValue.toString()));break;
			default: break;
		}
		
		it.setUpdated(true);
		
	}
}