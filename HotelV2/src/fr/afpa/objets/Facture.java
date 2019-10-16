package fr.afpa.objets;

import java.time.format.DateTimeFormatter;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.constants.FontStyles;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

public class Facture {

	private static String cheminDossierFacture = "ressources\\facture_pdf\\";

	public static String creationFacture(Reservation reservation, String type, int montantAPayer) {
		String cheminFacture = cheminDossierFacture + type+"reservation" + reservation.getNumeroReservation() + ".pdf";
		Document document = ItextGenerator.createDocument(cheminFacture);

		String paragraphe = "\n  FACTURE réservation n°" + reservation.getNumeroReservation() + "   \n";
		document.add(ItextGenerator.ajoutParagraphe(paragraphe, TextAlignment.CENTER, 20, new DottedBorder(3)));
		document.add(new Paragraph("\n"));
		
		
		if (type.equals("annulation")) {
			paragraphe = "ANNULATION";
			document.add(ItextGenerator.ajoutParagraphe(paragraphe, TextAlignment.CENTER, 25, Border.NO_BORDER)
					.setFontColor(ColorConstants.RED));
		}
		
		
		paragraphe = "Info client : \nNom : " + reservation.getClient().getPrenom() + "\nPrenom : "
				+ reservation.getClient().getNom() + "\nMail : " + reservation.getClient().getMail();

		document.add(ItextGenerator.ajoutParagraphe(paragraphe, TextAlignment.LEFT, 10, Border.NO_BORDER));
		document.add(new Paragraph("\n"));

		paragraphe = "date de séjour : du "
				+ reservation.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " au "
				+ reservation.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		document.add(ItextGenerator.ajoutParagraphe(paragraphe, TextAlignment.LEFT, 12, Border.NO_BORDER).setBold());

		paragraphe = "Info chambre : \nNuméro : " + reservation.getChambre().getNumero() + ", type : "
				+ reservation.getChambre().getTypeDeChambre() + "\nVue : " + reservation.getChambre().getVue()
				+ ", superficie " + reservation.getChambre().getSuperficie() + "\nliste options : \n";
		for (int i = 0; i < reservation.getChambre().getListeOptions().length; i++) {
			if (i != reservation.getChambre().getListeOptions().length - 1)
				paragraphe += reservation.getChambre().getListeOptions()[i] + ", ";
			else
				paragraphe += reservation.getChambre().getListeOptions()[i] + ".";
		}

		document.add(ItextGenerator.ajoutParagraphe(paragraphe, TextAlignment.LEFT, 12, Border.NO_BORDER));

		Table table = new Table(3, true);
		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Tarif journalier "));

		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Nombre de jours "));

		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Total "));

		table.addCell(ItextGenerator.remplissageCellDottedBorder("" + reservation.getChambre().getTarif()));
		table.addCell(ItextGenerator
				.remplissageCellDottedBorder("" + reservation.getDateFin().compareTo(reservation.getDateDebut())));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("" + reservation.getMontantTotal()));
		
		if(type.equals("nouvelle") || type.equals("annulation")) {
		table.addCell(ItextGenerator.remplissageCellDottedBorder(""));
		table.addCell(ItextGenerator.remplissageCellDottedBorder(""));
		table.addCell(ItextGenerator.remplissageCellDottedBorder(""));
		}
		
		if(type.equals("modification") || type.equals("liberation")) {
			table.addCell(ItextGenerator.remplissageCellDottedBorder("Montant déjà payer"));
			table.addCell(ItextGenerator.remplissageCellDottedBorder(""));
			table.addCell(ItextGenerator.remplissageCellDottedBorder(""));
			}

		if(montantAPayer>=0) {
		table.addCell(ItextGenerator.remplissageCellDottedBorder("Montant à payer : "));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("" + montantAPayer));
		table.addCell(ItextGenerator.remplissageCellDottedBorder(""));
		}
		else {
			table.addCell(ItextGenerator.remplissageCellDottedBorder("Montant remboursé : "));
			table.addCell(ItextGenerator.remplissageCellDottedBorder("" + -montantAPayer));
			table.addCell(ItextGenerator.remplissageCellDottedBorder(""));
			}

		document.add(table);
		document.close();
		return cheminFacture;
	}

	

}
