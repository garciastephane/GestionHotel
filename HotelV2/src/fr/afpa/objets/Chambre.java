package fr.afpa.objets;

import java.time.LocalDate;
import java.util.Arrays;

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

	public Reservation[] getListeReservationS() {
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
					&& listeReservations[i].getdateFin().isAfter(date)) {
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
	 * @param num�ro        : represente le num�ro de la chambre � afficher
	 * @param typeDeChambre : repr�sente le type de chambre
	 * @param superficie    : repr�sente la superficie
	 * @param vue           : r�presente la(les) vue(s) de la chambre
	 * @param chambre       occup�e : savoir si une chambre est occup�e � la date
	 * @param liste         reservations : liste des r�servations
	 */

	public void afficherEtatChambre() {
		System.out.println();
		System.out.println("Num�ro de la chambre: " + numero);
		System.out.println("Type de la chambre: " + typeDeChambre);
		System.out.println("Superficie de la chambres: " + superficie);
		System.out.println("Vue de la chambre " + vue);
		System.out.print("Chambre occup�e: ");
		if (isReserve(LocalDate.now())) {
			System.out.println("OUI");
		} else {
			System.out.println("NON");
		}

		System.out.println("Liste reservations de la chambre : ");

		for (int i = 0; i < listeReservations.length; i++) {
			if (listeReservations[i] != null) {
				System.out.print("Du " + listeReservations[i].getDateDebut());
				System.out.print(" au " + listeReservations[i].getdateFin());
				System.out.println(". Nom Client: " + listeReservations[i].getClient().getNom());
			}

		}
		System.out.println();
		System.out.println("---------------------------------------------------------------");
	}
}
