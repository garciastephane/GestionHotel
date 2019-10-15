package fr.afpa.objets;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Reservation {

	private LocalDate dateDebut;
	private LocalDate dateFin;
	private Chambre chambre;
	private Client client;
	private int montantTotal;
	private int numeroReservation;
	
	private static int nombreReservation;

	
	/**
	 * @param dateDebut
	 * @param dateFin
	 */
	public Reservation(LocalDate dateDebut_, LocalDate dateFin_, Chambre chambre_,Client client_) {
		dateDebut = dateDebut_;
		dateFin = dateFin_;
		setChambre(chambre_);
		client=client_;
		setMontantTotal(calculMontant());
		nombreReservation++;
		setNumeroReservation(nombreReservation);
	}

	@Override
	public String toString() {
		return "Reservation [dateDebut=" + dateDebut + ", dateFin=" + dateFin  + ", client="
				+ client + "]";
	}

	public void setLocalDateDebut(LocalDate dateDebut_) {
		dateDebut = dateDebut_;
	}

	public LocalDate getDateDebut() {
		return dateDebut;

	}

	public void setdateFin(LocalDate dateFin_) {
		dateFin = dateFin_;
	}

	public LocalDate getdateFin() {
		return dateFin;
	}

	public Chambre getChambre() {
		return chambre;
	}

	public void setChambre(Chambre chambre_) {
		chambre = chambre_;
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

	public void setMontantTotal(int montantTotal) {
		this.montantTotal = montantTotal;
	}

	/**
	 * Affiche la réservation
	 */
	public void affichage() {
		}
	

	/**
	 * methode qui effectue le payement et aliment le fichier de transactions
	 */
	public void payement(int montant, Scanner in, String cheminDossier) {
		String numeroCarte;
		if(montant>0) {
		System.out.println("Vous devez : " + montant + " , merci de donner votre numéro de carte");
		}
		else if(montant<0) {
			System.out.println("Nous vous remboursons : " + montant + " , merci de donner votre numéro de carte");
		}
		
		numeroCarte=in.nextLine();
		
		if(montant>0) {
		transaction(LocalDate.now(), "payement", montant, numeroCarte, cheminDossier);
		}else if(montant<0) {
			transaction(LocalDate.now(), "remboursement", montant, numeroCarte, cheminDossier);
		}
	}
	
	
	/**
	 * methode qui alimente le fichier de transaction
	 * @param date : date de la transaction
	 * @param nature : nature de la transaction
	 * @param valeur : valeur de la transaction
	 * @param numeroCarte : numéro de carte du client
	 * @param cheminDossier : chemin dosssier transaction
	 */
	public void transaction(LocalDate date, String nature, int valeur, String numeroCarte, String cheminDossier) {
		String cheminFichier=cheminDossier + "transactions" + date.format(DateTimeFormatter.ofPattern("ddMMyyyy")) +".txt";
		File f = new File(cheminFichier);
		if(!f.exists()) {
			Fichier.ecritureFichier(cheminFichier, "date;nature;valeur;numeroDeCarte", false);
		}
		Fichier.ecritureFichier(cheminFichier, date+";"+nature+";"+valeur+";"+numeroCarte, true);
	}
	
	public int calculMontant() {
		return dateFin.compareTo(dateDebut)*chambre.getTarif();
	}
	
	
}