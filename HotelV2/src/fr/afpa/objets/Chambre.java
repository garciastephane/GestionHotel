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

	public String getVueSuperficie() {
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
	 * @param date : LocalDate représentant la date où on veut savoir si la chambre
	 *             est réservé
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
	 * @param dateDebut : LocalDate représentant la date de début de la période où
	 *                  on veut savoir si la chambre est réservé
	 * @param dateFin   : LocalDate représentant la date de fin de la période où on
	 *                  veut savoir si la chambre est réservé
	 * @return true si la chambre est réservée au moins 1 jour pendant la période,
	 *         false si elle est libre pendant toute la période
	 */
	
	public boolean isReservePeriode(LocalDate dateDebut, LocalDate dateFin) {
		
		 LocalDate date = dateDebut;
		 while(!date.isAfter(dateFin)) {
			 if(isReserve(date)) {
				 return true;
			 }
			 date=date.plusDays(1);
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
		if(!(typeDeChambre.equals(chambre.typeDeChambre) 
				&&  superficie.equals(chambre.superficie) 
				&& tarif==chambre.tarif
				&& vue.equals(chambre.vue))) {
		return false;
		}
		for (int i = 0; i < listeOptions.length; i++) {
			if(!listeOptions[i].equals(chambre.listeOptions[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	

}