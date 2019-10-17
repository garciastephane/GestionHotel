package fr.afpa.objets;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.constants.FontStyles;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
public class ItextGenerator {
	
	/**
	 * Creation du document 
	 * @param chemin : chemin du document
	 * @return un Document 
	 */
	public static Document createDocument(String chemin) {
		
		PdfWriter writer;
		try {
			writer = new PdfWriter(chemin);
			PdfDocument pdf = new PdfDocument(writer);
			return new Document(pdf, PageSize.A4);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Erreur dans l'ouverture du document PDF" + e.getMessage());
		}
		return null;
		
	}
	
	/**
	 * creation d'une Image
	 * @param chemin : chemin de l'image
	 * @return une Image
	 */
	public static Image createImage(String chemin) {
		try {
			return new Image(ImageDataFactory.create(chemin));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erreur dans l'url de l'image" + e.getMessage());
		}
		return null;
		
	}
	public static Paragraph ajoutParagraphe(String paragraphe, TextAlignment alignement, float fontSize, Border border) {
		return new Paragraph(paragraphe).setTextAlignment(alignement).setFontSize(fontSize).setBorder(border);
	}
	
	public static Cell remplissageCellDottedBorder(String chaine) {
		Cell cell = new Cell().add(new Paragraph(chaine));
		cell.setWidth(40);
		cell.setHeight(20);
		cell.setBorder(Border.NO_BORDER);
		cell.setTextAlignment(TextAlignment.CENTER);
		cell.setBorderBottom(new DottedBorder(1));
		cell.setBorderTop(new DottedBorder(1));
		return cell;
	}
}