package fr.afpa.objets;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Reservation {

	private LocalDate dateDebut;
	private LocalDate dateFin;
	private Client client;
	private int montantTotal;
	private int numeroReservation;

	private static int nombreReservation;

	/**
	 * @param dateDebut
	 * @param dateFin
	 */
	public Reservation(LocalDate dateDebut_, LocalDate dateFin_, Client client_, int tarifJour) {
		dateDebut = dateDebut_;
		dateFin = dateFin_;
		client = client_;
		montantTotal = calculMontant(tarifJour);
		nombreReservation++;
		numeroReservation = nombreReservation;
	}

	@Override
	public String toString() {
		return "Reservation [dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", client=" + client + "]";
	}

	public void setDateDebut(LocalDate dateDebut_) {
		dateDebut = dateDebut_;
	}

	public LocalDate getDateDebut() {
		return dateDebut;

	}

	public void setDateFin(LocalDate dateFin_) {
		dateFin = dateFin_;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}


	public Client getClient() {
		return client;
	}

	public void setClient(Client client_) {
		client = client_;
	}

	public int getNumeroReservation() {
		return numeroReservation;
	}

	public void setNumeroReservation(int numeroReservation_) {
		numeroReservation = numeroReservation_;
	}

	public int getMontantTotal() {
		return montantTotal;
	}

	public void setMontantTotal(int montantTotal_) {
		montantTotal = montantTotal_;
	}

	/**
	 * Affiche la réservation
	 */
	public void affichage(Chambre chambre) {
		System.out.println("---------------------------------------------------------------");
		System.out.println("Votre réservation : ");
		System.out.println("Du " + dateDebut + " au " + dateFin);
		System.out.println("Chambre n°" + chambre.getNumero() + " type: " + chambre.getTypeDeChambre() + " superficie: "
				+ chambre.getSuperficie());
		System.out.println(" vue : " + chambre.getVue() + " tarif journalier : " + chambre.getTarif());
		System.out.println("Options de la chambre : ");
		for (int i = 0; i < chambre.getListeOptions().length; i++) {
			if (i != chambre.getListeOptions().length - 1) {
				System.out.print(chambre.getListeOptions()[i] + ", ");
			} else {
				System.out.println(chambre.getListeOptions()[i] + ".");
			}
		}
		System.out.println("Montant total : " + montantTotal + " euros");
		System.out.println("---------------------------------------------------------------");
	}

	/**
	 * methode qui effectue le payement et alimente le fichier de transactions
	 */
	public void payement(int montant, Scanner in, String cheminDossier) {
		String numeroCarte;
		
		if (montant > 0) {
			System.out.println("Vous devez : " + montant + " euros, merci de donner votre numéro de carte");
		} else if (montant < 0) {
			System.out.println("Nous vous remboursons : " + montant + " euros, merci de donner votre numéro de carte");
		}

		numeroCarte = in.nextLine();

		//alimentation du fichier transaction
		if (montant > 0) {
			transaction(LocalDate.now(), "payement", montant, numeroCarte, cheminDossier);
		} else if (montant < 0) {
			transaction(LocalDate.now(), "remboursement", montant, numeroCarte, cheminDossier);
		}
	}

	/**
	 * methode qui alimente le fichier de transaction
	 * 
	 * @param date          : date de la transaction
	 * @param nature        : nature de la transaction
	 * @param valeur        : valeur de la transaction
	 * @param numeroCarte   : numéro de carte du client
	 * @param cheminDossier : chemin dosssier transaction
	 */
	public void transaction(LocalDate date, String nature, int valeur, String numeroCarte, String cheminDossier) {
		
		String cheminFichier = cheminDossier + "transactions" + date.format(DateTimeFormatter.ofPattern("ddMMyyyy"))
				+ ".txt";
		
		File f = new File(cheminFichier);
		if (!f.exists()) {
			Fichier.ecritureFichier(cheminFichier, "date;nature;valeur;numeroDeCarte", false);
		}
		
		Fichier.ecritureFichier(cheminFichier, date + ";" + nature + ";" + valeur + ";" + numeroCarte, true);
	}

	public int calculMontant(int tarifJour) {
		return dateFin.compareTo(dateDebut) * tarifJour;
	}

	public boolean isEnCours() {
		LocalDate date =LocalDate.now();
		if(date.plusDays(1).isAfter(dateDebut) && date.isBefore(dateFin) ) {
			return true;
		}
		return false;
	}

	public void suppressionReservation() {
		// TODO Auto-generated method stub
		
	}
}