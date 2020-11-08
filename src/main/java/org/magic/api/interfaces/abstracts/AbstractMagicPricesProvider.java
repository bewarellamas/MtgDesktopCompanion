package org.magic.api.interfaces.abstracts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicDeck;
import org.magic.api.beans.MagicPrice;
import org.magic.api.interfaces.MTGPricesProvider;
import org.magic.services.MTGControler;
import org.magic.sorters.MagicPricesComparator;

public abstract class AbstractMagicPricesProvider extends AbstractMTGPlugin implements MTGPricesProvider {

	@Override
	public PLUGINS getType() {
		return PLUGINS.PRICER;
	}
	
	protected abstract List<MagicPrice> getLocalePrice(MagicCard card) throws IOException;
	
	
	@Override
	public Map<String, List<MagicPrice>> getPricesBySeller(List<MagicCard> cards) throws IOException {
		Map<String, List<MagicPrice>> map = new HashMap<>();
		
		
		for(MagicCard mc : cards)
		{
			List<MagicPrice> prices = getPrice(mc);
			
			for(MagicPrice mp : prices)
				map.computeIfAbsent(mp.getSeller(),v->new ArrayList<>()).add(mp);
		}
		return map;
	}
	
	
	private List<MagicPrice> retrieveMap(Map<MagicCard,Integer> map)
	{
			List<MagicPrice> ret = new ArrayList<>();
		
			map.entrySet().forEach(e->{
				try {
					MagicPrice p = getPrice(e.getKey()).stream().min(new MagicPricesComparator()).orElse(null);
					if(p!=null)
					{ 
						p.setMagicCard(e.getKey());
						p.setQty(e.getValue());
						p.setValue(p.getValue()*e.getValue());
						ret.add(p);
						notify(p);
					}
				} catch (IOException e1) {
					logger.error(e1);
				}
		});
		
		return ret;
	}
	
	@Override
	public List<MagicPrice> getPrice(MagicDeck d,boolean side) throws IOException {
		
		List<MagicPrice> ret = new ArrayList<>();
		ret.addAll(retrieveMap(d.getMain()));
			
			if(side)
				ret.addAll(retrieveMap(d.getSideBoard()));
		
		
		return ret;
	}
	
	
	public List<MagicPrice> getPrice(MagicCard card) throws IOException
	{
		return getLocalePrice(card)
								.stream()
								.map(p->{
											if(MTGControler.getInstance().getCurrencyService().isEnable()) {
												p.setValue(MTGControler.getInstance().getCurrencyService().convertTo(p.getCurrency(), p.getValue()));
												p.setCurrency(MTGControler.getInstance().getCurrencyService().getCurrentCurrency());
												p.setMagicCard(card);
											}
											return p;
										}
								).collect(Collectors.toList());
	}
	
	
	@Override
	public void alertDetected(List<MagicPrice> p) {
		// do nothing

	}

}
