package fr.afpa.objets;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.constants.FontStyles;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

public class Facture {

	/**
	 * Creation de la facture pour une nouvelle reservation
	 * 
	 * @param reservation          : Reservation
	 * @param employe              : Employe qui effectue la reservation
	 * @param chambre              : Chambre de la reservation
	 * @param cheminDossierFacture : chemin du dossier de la transaction
	 * @return
	 */
	public static String creationFacture(Reservation reservation, Employe employe, Chambre chambre,
			String cheminDossierFacture) {

		String cheminFacture = cheminDossierFacture + "reservation" + reservation.getNumeroReservation() + ".pdf";

		Document document = ItextGenerator.createDocument(cheminFacture);
		if(document==null) {
			return "";
		}

		document.add(entete(reservation.getNumeroReservation()));
		document.add(infoClientEtEmploye(reservation.getClient(), employe));
		document.add(dateSejour(reservation.getDateDebut(), reservation.getDateFin()));
		document.add(infosChambre(chambre));
		document.add(tableauPrix(reservation, chambre.getTarif(), 0));

		document.close();
		return cheminFacture;
	}

	/**
	 * Creation de la facture pour une modification de reservation
	 * 
	 * @param reservation          : Reservation
	 * @param employe              : Employe qui effectue la reservation
	 * @param chambre              : Chambre de la reservation
	 * @param cheminDossierFacture : chemin du dossier de la transaction
	 * @param ancienPrix           : ancien prix (montant verse par le client)
	 * @return
	 */
	public static String modificationFacture(Reservation reservation, Employe employe, Chambre chambre,
			String cheminDossierFacture, int ancienPrix) {
		String cheminFacture = cheminDossierFacture + "modificationReservation" + reservation.getNumeroReservation()
				+ ".pdf";

		Document document = ItextGenerator.createDocument(cheminFacture);

		document.add(entete(reservation.getNumeroReservation()).add("Modification"));
		document.add(infoClientEtEmploye(reservation.getClient(), employe));
		document.add(dateSejour(reservation.getDateDebut(), reservation.getDateFin()));
		document.add(infosChambre(chambre));
		document.add(tableauPrix(reservation, chambre.getTarif(), ancienPrix));

		document.close();
		return cheminFacture;

	}

	/**
	 * entete de la fature
	 * 
	 * @param numeroReservation : numero de la reservation
	 * @return : un Paragraph avec l'entete de la facture
	 */
	public static Paragraph entete(int numeroReservation) {
		Paragraph paragraphe = new Paragraph("\n  FACTURE réservation n°" + numeroReservation + "   \n");
		paragraphe.setTextAlignment(TextAlignment.CENTER);
		paragraphe.setFontSize(20);
		paragraphe.setBorder(new DottedBorder(3));
		return paragraphe;
	}

	/**
	 * info Client et Employe
	 * @param client : Client qui effectue la reservation
	 * @param employe : Employe qui effectue la reservation
	 * @return une Table avec les infos Client et Employe
	 */
	public static Table infoClientEtEmploye(Client client, Employe employe) {
		Table table = new Table(2, true);
		table.setBorder(Border.NO_BORDER);
		Paragraph paragraphe = new Paragraph("\nInfo client : \nNom : " + client.getNom() + "\nPrenom : "
				+ client.getPrenom() + "\nMail : " + client.getMail() + "\nLogin : " + client.getLogin());
		paragraphe.setFontSize(12);
		paragraphe.setTextAlignment(TextAlignment.LEFT);
		table.addCell(new Cell().add(paragraphe).setBorder(Border.NO_BORDER));

		paragraphe = new Paragraph("\nEmploye : \nNom : " + employe.getNom() + "\nPrenom : " + employe.getprenom());
		paragraphe.setFontSize(12);
		paragraphe.setTextAlignment(TextAlignment.RIGHT);
		table.addCell(new Cell().add(paragraphe).setBorder(Border.NO_BORDER));

		return table;
	}

	/**
	 * info date de sejour
	 * @param dateDebut : date de debut de la reservation
	 * @param dateFin : date de fin de la reservation
	 * @return un Paragraph avec les dates de sejour
	 */
	public static Paragraph dateSejour(LocalDate dateDebut, LocalDate dateFin) {
		Paragraph paragraphe = new Paragraph(
				"\nDate de séjour : du " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " au "
						+ dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

		paragraphe.setTextAlignment(TextAlignment.LEFT).setFontSize(12).setBold();
		return paragraphe;
	}

	/**
	 * info chambre reservee
	 * @param chambre : la Chambre reservee
	 * @return : un Paragraph avec les infos de la chambre reservee
	 */
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

	/**
	 * info prix, montant a payer ou rembourse
	 * @param reservation : Reservation
	 * @param tarifJournalier : tarif jouranlier de la Chambre
	 * @param ancienPrix : argent deja payer par le Client
	 * @return une Table avec les infos prix, montant a payer ou rembourse
	 */
	public static Table tableauPrix(Reservation reservation, int tarifJournalier, int ancienPrix) {
		Table table = new Table(3, true);

		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Tarif journalier "));
		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Nombre de jours "));
		table.addHeaderCell(ItextGenerator.remplissageCellDottedBorder(" Total "));

		table.addCell(ItextGenerator.remplissageCellDottedBorder("" + tarifJournalier));
		table.addCell(ItextGenerator
				.remplissageCellDottedBorder("" + Period.between(reservation.getDateDebut(), reservation.getDateFin()).getDays()));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("" + reservation.calculMontant(tarifJournalier) + "€"));

		table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));
		table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));

		if (ancienPrix != 0) {
			table.addCell(ItextGenerator.remplissageCellDottedBorder("accompte : "));
			table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));
			table.addCell(ItextGenerator.remplissageCellDottedBorder(ancienPrix + "€"));
		}
		if (reservation.calculMontant(tarifJournalier) - ancienPrix < 0) {
			table.addCell(ItextGenerator.remplissageCellDottedBorder(" Montant remboursé "));
			table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));
			table.addCell(ItextGenerator
					.remplissageCellDottedBorder((-1 * (reservation.calculMontant(tarifJournalier) - ancienPrix)) + "€"));
		} else {
			table.addCell(ItextGenerator.remplissageCellDottedBorder("Montant à payer : "));
			table.addCell(ItextGenerator.remplissageCellDottedBorder("  "));
			table.addCell(ItextGenerator
					.remplissageCellDottedBorder("" + (reservation.calculMontant(tarifJournalier) - ancienPrix) + "€"));
		}
		return table;
	}

}
