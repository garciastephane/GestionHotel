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
	/** modifie le numero de la chambre de l'hotel
	*
	* @param numero_ : le numero de la chambre
	*/
	public void setNumero(String numero_) {
		numero = numero_;
	}
	/**
	 * retourne le nouveau numero de la chambre
	 *
	 * @return : une chaine de caractere correspondant au numero de la chambre
	 */
	public String getNumero() {
		return numero;
	}
	/** modifie le type de la chambre de l'hotel
	*
	* @param typeDeChambre_ : le nouveau type de la chambre
	*/
	public void setTypeDeChambre(String typeDeChambre_) {
		typeDeChambre = typeDeChambre_;

	}
	/**
	 * retourne le nouveau type de la chambre
	 *
	 * @return : une chaine de caractere correspondant au type de chambre
	 */
	public String getTypeDeChambre() {
		return typeDeChambre;
	}
	/** modifie la superficie de la chambre de l'hotel
	*
	* @param superficie_ : la nouvelle superficie de la chambre
	*/
	public void setSuperficie(String superficie_) {
		superficie = superficie_;
	}
	/**
	 * retourne la nouvelle superficie de la chambre
	 *
	 * @return : une chaine de caractere correspondant a la superficie de la chambre
	 */
	public String getSuperficie() {
		return superficie;
	}
	/** modifie la vue de la chambre de l'hotel
	*
	* @param vue_ : la nouvelle vue de la chambre
	*/
	public void setVue(String vue_) {
		vue = vue_;
	}
	/**
	 * retourne la nouvelle vue de la chambre
	 *
	 * @return : une chaine de caractere correspondant a la vue de la chambre
	 */
	public String getVue() {
		return vue;
	}
	/** modifie le tarif de la chambre de l'hotel
	*
	* @param tarif_ : le nouveau tarif de la chambre
	*/
	public void setTarif(int tarif_) {
		tarif = tarif_;
	}
	/**
	 * retourne le nouveau tarif de la chambre
	 *
	 * @return : un entier correspondant au tarif de la chambre
	 */
	public int getTarif() {
		return tarif;
	}
	/** modifie la liste des options de la chambre de l'hotel
	*
	* @param listeOptions_ : la nouvelle liste d'options
	*/
	public void setListeOptions(String[] listeOptions_) {
		listeOptions = listeOptions_;
	}

	public String[] getListeOptions() {
		return listeOptions;
	}
	/** modifie le numero de la chambre de l'hotel
	*
	* @param numero_ : le numero de la chambre
	*/
	public void setListeReservations(Reservation[] listeReservations_) {
		listeReservations = listeReservations_;
	}
	/**
	 * retourne la liste des reservations
	 *
	 * @return : un tableau de la liste des reservations de tout les clients de l'hotel
	 */
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
	 * méthode qui permet de verifier si une chambre est reserve � pendant une
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

	/**
	 * methode qui permet d'ajouter une reservation
	 * 
	 * @param reservation : LocalDate représentant la date de début de la période
	 *       
	 * 
	 */
	public void ajoutReservation(Reservation reservation) {
		for (int i = 0; i < listeReservations.length; i++) {
			if (listeReservations[i] == null) {
				listeReservations[i] = reservation;
				break;
			}
		}

	}

	/**
	 * methode qui permet de verifier si deux chambres sont identiques
	 * 
	 * @param chambre : la chambre a comparer
	 
	 * @return false si les parametres sont differents 
	 *         true si identiques
	 */
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
	 * methode qui permet d'afficher les caracteristiques d'une chambre
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
				listeReservations[i].modifReservationPayement(this,listeReservations[i].getDateDebut(), LocalDate.now(),in,false);
				//la reservation n'est plus d'actualite, on la passe a null
				listeReservations[i] = null;
				return;
			}
		}
		
	}
}
