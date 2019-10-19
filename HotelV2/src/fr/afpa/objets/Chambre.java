package fr.afpa.objets;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class Chambre {

	private String numero;
	private String typeDeChambre;
	private String superficie;
	private String vue;
	private int tarif;
	private String[] listeOptions;
	private Reservation[] listeReservations;

	public Chambre(int numero_, String typeDeChambre_, String superficie_, String vue_, int tarif_,
			String[] listeOptions_) {
		numero = "" + numero_;
		typeDeChambre = typeDeChambre_;
		superficie = superficie_;
		vue = vue_;
		tarif = tarif_;
		listeOptions = listeOptions_;
		listeReservations = new Reservation[5];

	}

	@Override
	public String toString() {
		return "Chambre [numero=" + numero + ", typeDeChambre=" + typeDeChambre + ", superficie=" + superficie
				+ ", vue=" + vue + ", tarif=" + tarif + ", listeOptions=" + Arrays.toString(listeOptions)
				+ ", listeReservations=" + Arrays.toString(listeReservations) + "]";
	}

	public void setNumero(String numero_) {
		numero = numero_;
	}

	public String getNumero() {
		return numero;
	}

	public void setTypeDeChambre(String typeDeChambre_) {
		typeDeChambre = typeDeChambre_;

	}

	public String getTypeDeChambre() {
		return typeDeChambre;
	}

	public void setSuperficie(String superficie_) {
		superficie = superficie_;
	}

	public String getSuperficie() {
		return superficie;
	}

	public void setVue(String vue_) {
		vue = vue_;
	}

	public String getVue() {
		return vue;
	}

	public void setTarif(int tarif_) {
		tarif = tarif_;
	}

	public int getTarif() {
		return tarif;
	}

	public void setListeOptions(String[] listeOptions_) {
		listeOptions = listeOptions_;
	}

	public String[] getListeOptions() {
		return listeOptions;
	}

	public void setListeReservations(Reservation[] listeReservations_) {
		listeReservations = listeReservations_;
	}

	public Reservation[] getListeReservations() {
		return listeReservations;
	}

	/**
	 * methode qui permet de vérifier si une chambre est réservée à une date
	 * 
	 * @param date : LocalDate représentant la date où on veut savoir si la
	 *             chambre est réservé
	 * @return true si la chambre est réservée, false si elle est libre
	 * @throws InterruptedException
	 */
	public boolean isReserve(LocalDate date) {

		for (int i = 0; i < listeReservations.length; i++) {

			if (listeReservations[i] != null && listeReservations[i].getDateDebut().minusDays(1).isBefore(date)
					&& listeReservations[i].getDateFin().isAfter(date)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * méthode qui permet de vérifier si une chambre est réservée à pendant une
	 * période
	 * 
	 * @param dateDebut : LocalDate représentant la date de début de la période
	 *                  où on veut savoir si la chambre est réservé
	 * @param dateFin   : LocalDate représentant la date de fin de la période où
	 *                  on veut savoir si la chambre est réservé
	 * @return true si la chambre est réservée au moins 1 jour pendant la
	 *         période, false si elle est libre pendant toute la période
	 */

	public boolean isReservePeriode(LocalDate dateDebut, LocalDate dateFin) {

		LocalDate date = dateDebut;
		while (!date.isAfter(dateFin)) {
			if (isReserve(date)) {
				return true;
			}
			date = date.plusDays(1);
		}
		return false;
	}

	public void ajoutReservation(Reservation reservation) {
		for (int i = 0; i < listeReservations.length; i++) {
			if (listeReservations[i] == null) {
				listeReservations[i] = reservation;
				break;
			}
		}

	}

	public boolean isIdentique(Chambre chambre) {
		if (!(typeDeChambre.equals(chambre.typeDeChambre) && superficie.equals(chambre.superficie)
				&& tarif == chambre.tarif && vue.equals(chambre.vue))) {
			return false;
		}
		for (int i = 0; i < listeOptions.length; i++) {
			if (!listeOptions[i].equals(chambre.listeOptions[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Methode qui permet d'afficher les caract�ristiques d'une chambre
	 * 
	 */

	public void afficherEtatChambre() {
		if (isReserve(LocalDate.now())) {
			
			System.out.println();
			System.out.println("Numero de la chambre: " + numero);
			System.out.println("Type de la chambre: " + typeDeChambre);
			System.out.println("Superficie de la chambres: " + superficie);
			System.out.println("Vue de la chambre " + vue);
			System.out.print("Liste options: ");
			for(int i =0; i <listeOptions.length; i ++) {
				if (i!= listeOptions.length -1) {
					System.out.print(listeOptions[i] +  ", ");
				}
				else {
					System.out.println(listeOptions[i] +".");
				}
			}
			for (int i = 0; i < listeReservations.length; i++) {
				if (listeReservations[i]!= null && listeReservations[i].isEnCours() ) {
					System.out.println("Information du client: " + listeReservations[i].getClient().getNom() + " " + listeReservations[i].getClient().getPrenom() );
					System.out.println("Date de sejour : du " + listeReservations[i].getDateDebut() + " au " + listeReservations[i].getDateFin());
					
				}
			}
			System.out.println("---------------------------------------------------------------");
		} 
	}
	
	/**
	 * service de liberation de la chambre
	 * @param in : Le scanner pour la saisie du numero de carte
	 */
	public void liberationChambre(Scanner in) {
		
		//recherche de la r�servation en cours(celle qu'il faut liberer)
		for (int i = 0; i < listeReservations.length; i++) {
			
			if (listeReservations[i] != null && listeReservations[i].isEnCours()) {
				 
				//modification de la reservation en mettant la date de fin a aujourd'hui et on effectue le remboursement et alimentation du fichier transaction
				listeReservations[i].modifReservationPayement(tarif,listeReservations[i].getDateDebut(), LocalDate.now(),in);
				//la reservation n'est plus d'actualite, on la passe a null
				listeReservations[i] = null;
				return;
			}
		}
		
	}
}
