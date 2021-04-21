package org.beta;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.beta.MTGJsonPricer.VENDOR;
import org.beta.MTGJsonPricer.STOCK;
import org.beta.MTGJsonPricer.SUPPORT;
import org.magic.services.MTGControler;
import org.magic.services.MTGLogger;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class MTGJsonPricer {
	public enum SUPPORT {PAPER,MTGO}
	public enum STOCK {RETAIL, BUYLIST}
	public enum VENDOR {CARDKINGDOM,TCGPLAYER,CARDHOARDER,CARDMARKET}
	
	protected Logger logger = MTGLogger.getLogger(this.getClass());

	public static void main(String[] args) throws JsonParseException, IOException {
		File f = new File("C:\\Users\\Nicolas\\Google Drive\\test.json");
	//	File f = new File("D:\\Téléchargements\\AllPrices.json");
		MTGControler.getInstance();
		new MTGJsonPricer().buildPrice(f).forEach(System.out::println);
	}
	
	
	
	private Currency getCurrencyFor(VENDOR v)
	{
		if(v== VENDOR.CARDMARKET)
			return Currency.getInstance("EUR");
		
		return Currency.getInstance("USD");
		
	}
	
	
	private List<Data> buildPrice(File f) throws IOException {
		
		List<Data> ret = new ArrayList<>();
		
		
		try(var reader = new JsonReader(new FileReader(f)))
		{

				reader.beginObject();
				reader.nextName();
				Meta m = new Gson().fromJson(reader, Meta.class);
				logger.debug(m);
				
				reader.nextName();//data
				reader.beginObject(); 
				
				while(reader.hasNext())
				{	
					var data = new Data();
					data.setMtgjsonId(reader.nextName());
					reader.beginObject();
					String vendor = null;
					STOCK stock = null;
					SUPPORT support = null;
					
					while(reader.hasNext())
					{
						support = SUPPORT.valueOf(reader.nextName().toUpperCase());
						reader.beginObject();
						
						while(reader.hasNext())
						{
							vendor = reader.nextName();
							reader.beginObject();
							
							while(reader.hasNext())
							{
								
								if(reader.peek()==JsonToken.NAME)
								{
									String val = reader.nextName();
									if(val.equals("currency"))
									{
										reader.nextString();
									}
									else {
										stock = STOCK.valueOf(val.toUpperCase());	
									}
								}
								else
								{
									var p = new Prices();
									p.setVendor(VENDOR.valueOf(vendor.toUpperCase()));
									p.setSupport(support);
									p.setStock(stock);
									p.setCurrency(getCurrencyFor(p.getVendor())); 
									
									
									reader.beginObject();
									while(reader.hasNext())
									{
										p.setFoil(reader.nextName().equals("foil"));
										reader.beginObject();
										while(reader.hasNext())
										{
											p.getStockPrices().put(reader.nextName(), reader.nextDouble());
										} // fin boucle map date/prix
										reader.endObject();
									}//fin boucle Foil/Normal
									data.getPrices().add(p);
									reader.endObject();
								}
							}//fin de boucle buylist/retail/Currency
							reader.endObject();	
						}//fin boucle retailer;
						reader.endObject();	
					}//fin boucle mtgjsonIds
					ret.add(data);
					reader.endObject();
				}//fin boucle data
				
			}
		
			return ret;
		
	}
}

class Meta
{
	private String date;
	private String version;
	
	@Override
	public String toString() {
		return getDate() + " " + getVersion();
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
} 


class Prices
{
	private boolean foil;
	private Map<String,Double> stockPrices;
	private Currency currency;
	private STOCK stock;
	private VENDOR vendor;
	private SUPPORT support;
	
	public SUPPORT getSupport() {
		return support;
	}

	public void setSupport(SUPPORT support) {
		this.support = support;
	}

	public VENDOR getVendor() {
		return vendor;
	}

	public void setVendor(VENDOR v) {
		this.vendor = v;
	}

	public void setStock(STOCK stock) {
		this.stock = stock;
	}
	
	 public STOCK getStock() {
		return stock;
	}
	
	
	@Override
	public String toString() {
		var temp = new StringBuilder();
		temp.append(vendor).append(" ").append(support).append(" ").append(stock).append(" " ).append(foil?"Foil":"Normal").append(" ").append(currency).append(" ").append(getStockPrices().size());
		return temp.toString();
	}
	
	public Prices() {
		stockPrices = new HashMap<>();
	}
	
	public boolean isFoil() {
		return foil;
	}
	public void setFoil(boolean foil) {
		this.foil = foil;
	}
	
	public Map<String, Double> getStockPrices() {
		return stockPrices;
	}
	
	public void setStockPrices(Map<String, Double> stockPrices) {
		this.stockPrices = stockPrices;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = Currency.getInstance(currency);
	}
	
	
	public Currency getCurrency() {
		return currency;
	}
	
	
}



class Data
{
	private String mtgjsonId;

	private List<Prices> prices;
	
	public Data()
	{
		prices = new ArrayList<>();
	}
	
	public List<Prices> getPrices() {
		return prices;
	}
	
	public void setPrices(List<Prices> prices) {
		this.prices = prices;
	}
	

	@Override
	public String toString() {
		var temp = new StringBuilder();
		temp.append(mtgjsonId).append(":");
		
		for(Prices p : getPrices())
			temp.append("\n\t").append(p);
		
			
		return temp.toString();
	}

	public String getMtgjsonId() {
		return mtgjsonId;
	}
	public void setMtgjsonId(String mtgjsonId) {
		this.mtgjsonId = mtgjsonId;
	}
	
}

