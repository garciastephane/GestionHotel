package fr.afpa.objets;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
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
	/** modifie la date de debut de la reservation du client
	*
	* @param dateDebut_ : la date de debut de la reservation
	*/
	public void setDateDebut(LocalDate dateDebut_) {
		dateDebut = dateDebut_;
	}
	/**
	 * retourne la nouvelle date de debut de la reservation
	 *
	 * @return : un LocalDate correspondant a la date de debut de la reservation
	 */
	public LocalDate getDateDebut() {
		return dateDebut;

	}
	/** modifie la date de fin de la reservation du client
	*
	* @param dateFin_ : la date de fin de la reservation
	*/
	public void setDateFin(LocalDate dateFin_) {
		dateFin = dateFin_;
	}
	/**
	 * retourne la nouvelle date de fin de la reservation
	 *
	 * @return : un LocalDate correspondant a la date de fin de la reservation
	 */
	public LocalDate getDateFin() {
		return dateFin;
	}
	/**
	 * retourne le nouveau client de la reservation
	 *
	 * @return : un Client correspondant au nouveau client de la reservation
	 */
	public Client getClient() {
		return client;
	}
	/** modifie le client de la reservation
	*
	* @param client_ : le client de la reservation
	*/
	public void setClient(Client client_) {
		client = client_;
	}
	/**
	 * retourne le nouveau numero de la reservation  du client de la chambre
	 *
	 * @return : un entier correspondant au numero de la reservation de la chambre
	 */
	public int getNumeroReservation() {
		return numeroReservation;
	}
	/** modifie le numero de la reservation du client de la chambre
	*
	* @param numero de la reservation_ : le numero de la reservation du client
	*/
	public void setNumeroReservation(int numeroReservation_) {
		numeroReservation = numeroReservation_;
	}
	/**
	 * retourne le nouveau montant total de la reservation du client
	 *
	 * @return : un entier correspondant au montant total de la reservation du client
	 */
	public int getMontantTotal() {
		return montantTotal;
	}
	/** modifie le montant total de la reservation du client de la chambre
	*
	* @param montant total_ : le montant total de la reservation du client
	*/
	public void setMontantTotal(int montantTotal_) {
		montantTotal = montantTotal_;
	}

	/**
	 * Affiche la reservation
	 */
	public void affichage(Chambre chambre) {
		System.out.println("---------------------------------------------------------------");
		System.out.println("Votre reservation : ");
		System.out.println("Du " + dateDebut + " au " + dateFin);
		System.out.println("Chambre numero :" + chambre.getNumero() + " type: " + chambre.getTypeDeChambre() + " superficie: "
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
			System.out.println("Vous devez : " + montant + " euros, merci de donner votre numero de carte");
		} else if (montant < 0) {
			System.out.println(
					"Nous vous remboursons : " + montant * -1 + " euros, merci de donner votre numero de carte");
		}

		numeroCarte = in.nextLine();

		// alimentation du fichier transaction
		if (montant > 0) {
			transaction(LocalDate.now(), "payement", montant, numeroCarte, cheminDossier);
		} else if (montant < 0) {
			transaction(LocalDate.now(), "remboursement", montant * -1, numeroCarte, cheminDossier);
		}
	}

	/**
	 * methode qui alimente le fichier de transaction
	 * 
	 * @param date          : date de la transaction
	 * @param nature        : nature de la transaction
	 * @param valeur        : valeur de la transaction
	 * @param numeroCarte   : numero de carte du client
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
		
		return Period.between(dateDebut, dateFin).getDays() * tarifJour;
	}
	/**
	 * methode qui permets de confirmer si une reservation est en cours
	 * 	 
	 */

	public boolean isEnCours() {
		LocalDate date = LocalDate.now();
		if (date.plusDays(1).isAfter(dateDebut) && date.isBefore(dateFin)) {
			return true;
		}
		return false;
	}

	/**
	 * methode qui modifie le paiement de la reservation
	 * @param chambre              :  chambre de la reservation
	 * @param dateDebutNouv          : date de debut de la nouvelle reservation
	 * @param dateFinNouv            : date de fin de la nouvelle reservation
	 * @param in                     : scanner qui recupere la transaction
	 *  @param modif                     : true si modification reservation
	 */
	public void modifReservationPayement( Chambre chambre, LocalDate dateDebutNouv, LocalDate dateFinNouv, Scanner in , boolean modif) {
		int montantInitial = calculMontant(chambre.getTarif());
		dateDebut = dateDebutNouv;
		dateFin = dateFinNouv;
		
		if(dateFin.equals(LocalDate.now())) { // la reservation est termine
			client.setNombreDeReservations(client.getNombreDeReservations()-1);
		}
		if(modif) {
			affichage(chambre);
		}
		int nouveauMontant = calculMontant(chambre.getTarif()) - montantInitial;
		payement(nouveauMontant, in, "ressources\\transactions\\");

	}
}
	
	