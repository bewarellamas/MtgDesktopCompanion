package org.magic.api.exports.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.NotImplementedException;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicCardStock;
import org.magic.api.beans.MagicDeck;
import org.magic.api.interfaces.abstracts.AbstractCardExport;
import org.magic.services.MTGDeckManager;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;


public class DCIDeckSheetExport extends AbstractCardExport {

	private static final String FILL_CONTINUED_LANDS = "FILL_CONTINUED_LANDS";
	private static final String FORCED_DATE = "FORCED_DATE";
	private static final String DATE_FORMAT = "DATE_FORMAT";
	private static final String LOCATION = "LOCATION";
	private static final String DCI_NUMBER = "DCI_NUMBER";
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String LAST_NAME = "LAST_NAME";
	private static final String EVENT_NAME = "EVENT_NAME";
	private static final String DECK_DESIGNER = "DECK_DESIGNER";
	private String space = "          ";

	@Override
	public MODS getMods() {
		return MODS.EXPORT;
	}


	@Override
	public MagicDeck importDeck(String f,String n) throws IOException {
		throw new NotImplementedException("Can't generate deck from DCI Sheet");
	}

	@Override
	public String getFileExtension() {
		return ".pdf";
	}
	
	
	private Paragraph createParagraphe(String text, float x, float y)
	{

		try {
			PdfFont font  = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		
			return new Paragraph(text)
					.setFont(font)
					.setFontSize(11)
					.setFixedPosition(x, y, 200)
					.setTextAlignment(TextAlignment.LEFT);
			
			
		} catch (IOException e) {
			logger.error(e);
			
			return new Paragraph(text)
					.setFontSize(11)
					.setFixedPosition(x, y, 200)
					.setTextAlignment(TextAlignment.LEFT);
		}	
	}
	
	
	@Override
	public void exportDeck(MagicDeck deck, File dest) throws IOException {
		
		try (
			 PdfDocument pdfSrc = new PdfDocument(new PdfReader(this.getClass().getResource("/data/mtg_constructed_deck_registration_sheet_pdf1.pdf").openStream()));
			 Document docSrc = new Document(pdfSrc);
			 PdfDocument pdfDest = new PdfDocument(new PdfWriter(dest));
		     Document docDest = new Document(pdfDest)
			)
			{
			
			pdfDest.setDefaultPageSize(PageSize.A4);
			pdfSrc.copyPagesTo(1,1,pdfDest);
	
			float h = pdfDest.getDefaultPageSize().getHeight();
			float w = pdfDest.getDefaultPageSize().getWidth();
			
			// HEADER
			docDest.add(createParagraphe(getString(LAST_NAME).substring(0, 1).toUpperCase(), w-35, h-103));
		
			
			if (!getString(FORCED_DATE).equalsIgnoreCase(""))
				docDest.add(createParagraphe(getString(FORCED_DATE), w/3.2f, h-127));
			else
				docDest.add(createParagraphe(new SimpleDateFormat(getString(DATE_FORMAT)).format(new Date()), w/3.2f, h-127));
			
			docDest.add(createParagraphe(getString(LOCATION),w/3.2f,h-150));
			
			docDest.add(createParagraphe(getString(EVENT_NAME),w/1.40f,h-127));
			docDest.add(createParagraphe(deck.getName(),w/1.40f,h-150));
			if (getString(DECK_DESIGNER).equals(""))
				docDest.add(createParagraphe(getString(LAST_NAME) + " " + getString(FIRST_NAME),w /1.40f, h-175));
			else
				docDest.add(createParagraphe(getString(DECK_DESIGNER),w /1.40f, h - 175));
			
			
			// MAIN DECK
			int count = 0;
			for (MagicCard mc : deck.getMain().keySet()) {
				docDest.add(createParagraphe(deck.getMain().get(mc) + space + mc.getName(),w/6.4f,h-240-count));
				count += 18;
				notify(mc);
			}

			// CONTINUED and BASIC LAND
			if (getBoolean(FILL_CONTINUED_LANDS)) 
			{
				count = 0;
				for (MagicCard mc : deck.getMain().keySet()) {
					if (mc.isBasicLand()) {
						docDest.add(createParagraphe(deck.getMain().get(mc) + space + mc.getName(),w/1.65f,h-240-count));
						count += 18;
					}
					notify(mc);
				}
				
			}
			
			// SIDEBOARD
			count = 0;
			for (MagicCard mc : deck.getSideBoard().keySet()) {
				docDest.add(createParagraphe(deck.getSideBoard().get(mc) + space + mc.getName(),w/1.65f,h-474-count));
				notify(mc);
				count += 18;
			}
			
			// BOTTOM card count
			docDest.add(createParagraphe(String.valueOf(deck.getMainAsList().size()),(w/2)-25,40));
			docDest.add(createParagraphe(String.valueOf(deck.getSideAsList().size()),w-55,95));
			
			// LEFT TEXT
			Paragraph p = createParagraphe(getString(LAST_NAME),60,90);
					  p.setRotationAngle(1.5708f);
			docDest.add(p);
			
			p = createParagraphe(getString(FIRST_NAME),60,300);
			p.setRotationAngle(1.5708f);
			docDest.add(p);
			
			
			
			String dci = getString(DCI_NUMBER);
			count = 0;
			for (int i = 0; i < dci.length(); i++) {
				char c = dci.charAt(i);
				
				p = createParagraphe(String.valueOf(c),60,(430 + count));
				p.setRotationAngle(1.5708f);
				docDest.add(p);
				count += 22;
			}
			
			
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}

		
		

	}

	@Override
	public String getName() {
		return "DCI Deck Sheet";
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(PDFExport.class.getResource("/icons/plugins/sheet.png"));
	}

	@Override
	public List<MagicCardStock> importStock(String content) throws IOException {
		throw new NotImplementedException("Can't import stock from DCI Sheet");
	}

	@Override
	public void initDefault() {
		setProperty(EVENT_NAME, "my Event");
		setProperty(DECK_DESIGNER, "MTGDesktopCompanion");
		setProperty(LAST_NAME, "My name");
		setProperty(FIRST_NAME, "My first name");
		setProperty(DCI_NUMBER, "0000000000");
		setProperty(LOCATION, "fill it");
		setProperty(DATE_FORMAT, "dd/MM/YYYY");
		setProperty(FORCED_DATE, "");
		setProperty(FILL_CONTINUED_LANDS, "true");

	}
	
	
}
