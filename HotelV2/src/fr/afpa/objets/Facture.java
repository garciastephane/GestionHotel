package fr.afpa.objets;

import java.time.LocalDate;
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

	public static String creationFacture(Reservation reservation, Employe employe, Chambre chambre) {

		String cheminFacture = cheminDossierFacture +  "reservation" + reservation.getNumeroReservation()
				+ ".pdf";

		Document document = ItextGenerator.createDocument(cheminFacture);

		document.add(entete(reservation.getNumeroReservation()));

		document.add(infosClient(reservation.getClient()));

		document.add(dateSejour(reservation.getDateDebut(), reservation.getDateFin()));

		document.add(infosChambre(chambre));

		document.add(tableauRes(reservation, chambre.getTarif()));
		 
		document.close();
		return cheminFacture;
	}

	public static Paragraph entete(int numeroReservation) {

		Paragraph paragraphe = new Paragraph("\n  FACTURE réservation n°" + numeroReservation + "   \n");
		paragraphe.setTextAlignment(TextAlignment.CENTER);
		paragraphe.setFontSize(20);
		paragraphe.setBorder(new DottedBorder(3));
		return paragraphe;

	}

	public static Paragraph infosClient(Client client) {
		Paragraph paragraphe = new Paragraph("\nInfo client : \nNom : " + client.getPrenom() + "\nPrenom : "
				+ client.getNom() + "\nMail : " + client.getMail()+ "\nLogin : " + client.getLogin());
		paragraphe.setFontSize(12);
		paragraphe.setTextAlignment(TextAlignment.LEFT);
		return paragraphe;

	}

	public static Paragraph dateSejour(LocalDate dateDebut, LocalDate dateFin) {
		Paragraph paragraphe = new Paragraph(
				"\nDate de séjour : du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " au "
						+ dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

		paragraphe.setTextAlignment(TextAlignment.LEFT).setFontSize(12).setBold();
		return paragraphe;

	}

	public static Paragraph infosChambre(Chambre chambre) {
		Paragraph paragraphe = new Paragraph("\nInfo chambre : \nNuméro : " + chambre.getNumero() + ", type : "
				+ chambre.getTypeDeChambre() + "\nVue : " + chambre.getVue() + ", superficie " + chambre.getSuperficie()
				+ "\nliste options : \n");
		for (int i = 0; i < chambre.getListeOptions().length; i++) {
			if (i != chambre.getListeOptions().length - 1)
				paragraphe.add(chambre.getListeOptions()[i] + ", ");
			else
				paragraphe.add(chambre.getListeOptions()[i] + ".\n\n\n  ");
		}
		paragraphe.setTextAlignment(TextAlignment.LEFT).setFontSize(12);

		return paragraphe;

	}

	public static Table tableauRes(Reservation reservation, int tarif) {
		Table table = new Table(3, true);
		
		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Tarif journalier "));

		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Nombre de jours "));

		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Total "));

		table.addCell(ItextGenerator.remplissageCellDottedBorder("" + tarif));
		table.addCell(ItextGenerator
				.remplissageCellDottedBorder("" + reservation.getDateFin().compareTo(reservation.getDateDebut())));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("" + reservation.calculMontant(tarif)));

		table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));

		table.addCell(ItextGenerator.remplissageCellDottedBorder("Montant à payer : "));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("" + reservation.calculMontant(tarif)));

		return table;
	}

}
