
package fr.afpa.objets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Hotel {
	private Chambre[] listeChambres;
	private String nom;
	private Employe[] employe;
	private String cheminDossierTransaction; // chemin dossier puis ajout du fichier de lors de l'ecriture de la
												// transaction dans le fichier du jour
	private String cheminFichierMdp;

	public Hotel(String nom_) {
		nom = nom_;
		cheminDossierTransaction = "ressources\\transactions\\";
		cheminFichierMdp = "ressources\\mdp.txt";
		// initialisation listeChambres via ficier csv
		creationHotel("ressources\\ListeChambres_V3.csv");

		// creation employe admin réintialisation des employe à chaque demarage du
		// programme
		employe = new Employe[1];
		employe[0] = new Employe("GH000", "nom admin", "prenom admin");

		// ecriture login/mot de passe de l'employe dans un fichier texte
		Fichier.ecritureFichier(cheminFichierMdp, "fonction;login;mdp;mdpAdmin", false);
		Fichier.ecritureFichier(cheminFichierMdp, "employe;" + "GH000" + ";" + "admin" + ";" + "admin", true);

		// appel methode de gestion de l'hotel;
		gestionHotel();
	}

	public void setListeChambres(Chambre[] listeChambres_) {
		listeChambres = listeChambres_;

	}

	public Chambre[] getListeChambres() {
		return listeChambres;
	}

	public void setNom(String nom_) {
		nom = nom_;
	}

	public String getNom() {
		return nom;
	}

	public void setEmploye(Employe[] employe_) {
		employe = employe_;
	}

	public Employe[] getEmploye() {
		return employe;
	}

	public void setCheminDossierTransaction(String cheminDossierTransaction_) {
		cheminDossierTransaction = cheminDossierTransaction_;
	}

	public String getCheminDossierTransaction() {
		return cheminDossierTransaction;
	}

	public void setCheminFichierMdp(String cheminFichierMdp_) {
		cheminFichierMdp = cheminFichierMdp_;
	}

	public String getCheminFichierMdp() {
		return cheminFichierMdp;
	}

	/**
	 * lecture du fichier et cr�ation des chambres de l'hotel
	 * 
	 * @param chemin : chemin absolue du fichier .csv contenant les informations sur
	 *               les chambres
	 */
	private void creationHotel(String chemin) {
		String[] lignes;
		String[] ligne;
		int nbChambres = 0;
		int indexChambre = 0;

		// lecture de fichier et stockage de chaque lignes du fichier dans un tableau
		lignes = Fichier.lecture(chemin);

		// calcul du nombre de chambres � cr�er
		for (int i = 0; i < lignes.length; i++) {
			ligne = lignes[i].split(";");
			if (Controle.isNumerique(ligne[5], ligne[5].length())) {
				nbChambres += Integer.parseInt(ligne[5]);
			}
		}
		// initialisation du tableau des chambres avec le nombre de chambre
		listeChambres = new Chambre[nbChambres];

		// lecture du fichier pour instancier les chambres de l'hotel
		for (int i = 0; i < lignes.length; i++) {
			ligne = lignes[i].split(";");
			if (Controle.isNumerique(ligne[5], ligne[5].length())) {
				// nombre de chambres � cr�er pour chaque type
				nbChambres = Integer.parseInt(ligne[5]);
				for (int j = 0; j < nbChambres; j++) {
					listeChambres[indexChambre] = new Chambre((indexChambre + 1), ligne[0], ligne[1], ligne[2],
							Integer.parseInt(ligne[4]), ligne[6].split("\\|"));
					indexChambre++;
				}
			}
		}

	}

	public void gestionHotel() {
		Scanner in = new Scanner(System.in);
		// on quitte le programme via le menu authentification (pour pouvoir
		// s'authenfier avec un client une fois cr��)
		while (menuAuthentification(in))
			;

	}

	/**
	 * menu authentification (authentification(employ� ou client), cr�� un employ�,
	 * quitter le programme
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 * @return false si l'utiliateur d�cide de quitter, false sinon
	 */
	public boolean menuAuthentification(Scanner in) {

		System.out.print("tapez 1 si vous voulez cr�er un nouvel employ�, tapez \"Q\" pour quitter \nLogin : ");
		String choix = in.nextLine();

		if (choix.equals("1")) { // creation employ�
			creationEmploye(in);
			return true;
		}

		if (choix.equals("Q")) { // quitter programme
			return false;
		}

		if (Controle.isNumerique(choix, 10)) { // authentification d'un client

			if (!Controle.isUnique(choix, listeLoginClient())) { // authentification r�ussit (client existant)

				afficherReservationClient(choix);

				/*
				 * for (int i = 0; i < listeClients.length; i++) { // recup�ration du client
				 * dans la liste client
				 * 
				 * if (listeClients[i].getLogin().equals(choix)) {
				 * listeClients[i].afficherReservations(); // affiche ces r�servations ou
				 * message erreur } }
				 */
			} else { // authentification mauvaise (client non existant)
				System.out.println("Erreur authentification client");
			}
			return true;
		}

		if (Controle.isAlphaNumerique(choix, 5)) { // authentification employ�

			if (Controle.authentificationEmploye(in, choix, cheminFichierMdp)) { // authentification via mot de passe
				int indiceEmploye = 0;
				for (int i = 0; i < employe.length; i++) {
					if (employe[i].getlogin().equals(choix)) {
						indiceEmploye = i;
						break;
					}
				}

				affichageMenu(); // authentification r�ussit
				while (choixEmploye(in, indiceEmploye)) { // affichage menu employ� jusqu' a ce qu'il d�cide de quitter
					affichageMenu();
				}
			} else {
				System.out.println("Erreur d'authentification"); // authentification mauvaise(login ou mot de passe non
																	// valide)
			}
		}
		return true;

	}

	/**
	 * Affichage Menu employ�
	 */
	public void affichageMenu() {
		System.out.println("---------------------   MENU HOTEL CDA JAVA  ------------------");
		System.out.println("");
		System.out.println("A- Afficher l'�tat de l'h�tel");
		System.out.println("B- Afficher le nombre de chambres r�serv�es");
		System.out.println("C- Afficher le num�ro de chambres libres");
		System.out.println("D- Afficher le num�ro de la premi�re chambre vide");
		System.out.println("E- Afficher le num�ro de la derni�re chambre vide");
		System.out.println("F- R�server une chambre");
		System.out.println("G- Lib�rer une chambre");
		System.out.println("H- Modifier une r�servation");
		System.out.println("I- Annuler une r�servation");
		System.out.println("Q- Quitter");
		System.out.println("");
		System.out.println("---------------------------------------------------------------");
		System.out.println("Votre choix :");
	}

	/**
	 * Traitement du choix employ�
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 * @return : false si l'employ� veut quitter le Menu, true sinon
	 */
	public boolean choixEmploye(Scanner in, int indiceEmploye) {
		String choix = in.nextLine();

		switch (choix) {
		case "A":
			afficherEtatHotel();
			break;
		case "B":
			afficherNombreChambresReserves();
			break;
		case "C":
			afficherNombreChambresLibres();
			break;
		case "D":
			afficherNumeroPremiereChambreLibre();
			break;
		case "E":
			afficherNumeroDerniereChambreLibre();
			break;
		case "F":
			employe[indiceEmploye].reservationChambre(this, in);
			break;
		case "G":
			employe[indiceEmploye].liberationChambre(in, this);
			break;
		case "H":
			break;
		case "I":
			break;
		case "Q":
			return false;
		default:
			System.out.println("Veuillez entrer un choix valide");
		}
		return true;

	}

	/**
	 * methode de cr�ation de l'employ� de l'hotel
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 */
	private void creationEmploye(Scanner in) {
		String login;
		String motDePasse;
		String nom;
		String prenom;

		// Demande des informations pour la cr�ation de l'employ�
		System.out.println("Entrer le login de l'employ� : ");
		login = Saisie.saisieLoginEmploye(in);
		System.out.println("Entrer le mot de passe de l'employ� : ");
		motDePasse = Saisie.saisieNonVide(in);
		System.out.println("Entrer le nom de l'employ� : ");
		nom = Saisie.saisieAlphabetic(in);
		System.out.println("Entrer le prenom de l'employ� : ");
		prenom = Saisie.saisieAlphabetic(in);

		// ajout de l'employ� de la banque

		Employe employeNouv = new Employe(login, nom, prenom);
		Employe[] employesTemp = new Employe[employe.length];
		for (int i = 0; i < employesTemp.length; i++) {
			employesTemp[i] = employe[i];
		}

		employe = new Employe[employe.length + 1];
		for (int i = 0; i < employesTemp.length; i++) {
			employe[i] = employesTemp[i];
		}
		employe[employe.length - 1] = employeNouv;
		// ajout du login et mot de passe de l'employ� au fichier mot de passe
		Fichier.ecritureFichier(cheminFichierMdp, "employe;" + login + ";" + motDePasse + ";" + motDePasse, true);

	}

	/**
	 * retourne la liste des logins Client ayant cr�er un profil dans l'hotel
	 * 
	 * @return un tableau de chaine de caract�res repr�sentant la liste des login
	 *         d�j� existant
	 */

	public Client[] getListeClients() {
		// creation d'un tableau de login client de la taille maximum qu'il peut avoir
		String[] listeLoginExistant = listeLoginClient();
		Client[] listeClient = new Client[listeLoginExistant.length];

		if (listeLoginExistant[0].equals("")) {
			return null;
		}

		for (int indiceLogin = 0; indiceLogin < listeLoginExistant.length; indiceLogin++) {

			for (int indiceChambre = 0; indiceChambre < listeChambres.length; indiceChambre++) {

				for (int indiceReserv = 0; indiceReserv < listeChambres[indiceChambre]
						.getListeReservations().length; indiceReserv++) {

					if (listeChambres[indiceChambre].getListeReservations()[indiceReserv] != null
							&& listeChambres[indiceChambre].getListeReservations()[indiceReserv].getClient().getLogin()
									.equals(listeLoginExistant[indiceLogin])) {
						listeClient[indiceLogin] = listeChambres[indiceChambre].getListeReservations()[indiceReserv]
								.getClient();

					}
				}
			}
		}

		return listeClient;
	}

	public String[] listeLoginClient() {
		// creation d'un tableau de login client de la taille maximum qu'il peut avoir
		String[] listeLoginClientTemp = new String[listeChambres.length * 5];
		int nbLogin = 0;
		for (int i = 0; i < listeChambres.length; i++) {
			for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) {
				// ajout d'un login si il n'est pas deja present dans le tableau
				if (listeChambres[i].getListeReservations()[j] != null && Controle.isUnique(
						listeChambres[i].getListeReservations()[j].getClient().getLogin(), listeLoginClientTemp)) {
					listeLoginClientTemp[nbLogin] = listeChambres[i].getListeReservations()[j].getClient().getLogin();
					nbLogin++;

				}
			}
		}

		if (nbLogin == 0) {
			return new String[] { "" };
		}

		// creation d'un tableau de login de la taille contenant tous les logins
		// différents reelement present dans l'hotel
		String[] listeLoginClient = new String[nbLogin];
		for (int i = 0; i < listeLoginClient.length; i++) {
			listeLoginClient[i] = listeLoginClientTemp[i];
		}

		return listeLoginClient;
	}

	public Client clientReservation(String loginClient) {

		for (int i = 0; i < listeChambres.length; i++) {
			for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) {
				if (listeChambres[i].getListeReservations()[j] != null
						&& listeChambres[i].getListeReservations()[j].getClient().getLogin().equals(loginClient)) {
					return listeChambres[i].getListeReservations()[j].getClient();
				}
			}
		}
		return null;
	}

	/**
	 * Affiche la liste de clients de l'hotel
	 */

	public void afficherListeClient() {
		Client[] listeClients = getListeClients();
		for (int i = 0; i < listeClients.length; i++) {
			System.out.println(listeClients[i]);
		}
	}

	public void afficherNombreChambresReserves() {
		int nombreChambresReserves = 0;
		for (int i = 0; i < listeChambres.length; i++) {

			if (listeChambres[i].isReserve(LocalDate.now())) {
				nombreChambresReserves++;
			}

		}

		System.out.println("il y a " + nombreChambresReserves + " chambres reservees");

	}

	public void afficherNombreChambresLibres() {
		int nombreChambresLibres = 0;
		for (int i = 0; i < listeChambres.length; i++) {

			if (!listeChambres[i].isReserve(LocalDate.now())) {
				nombreChambresLibres++;
			}
		}

		System.out.println("il y a " + nombreChambresLibres + " chambres libres");

	}

	public void afficherNumeroPremiereChambreLibre() {

		for (int i = 0; i < listeChambres.length; i++) {

			if (!listeChambres[i].isReserve(LocalDate.now())) {
				System.out.println("Le num�ro de la premi�re chambre libre est : " + listeChambres[i].getNumero());
				return;
			}
		}

	}

	public void afficherNumeroDerniereChambreLibre() {

		for (int i = listeChambres.length - 1; i >= 0; i--) {
			if (!listeChambres[i].isReserve(LocalDate.now())) {
				System.out.println("Le num�ro de la derni�re chambre libre est : " + listeChambres[i].getNumero());
				return;
			}
		}
	}

	public Chambre[] listeChambreDispo(LocalDate dateDebut, LocalDate dateFin) {
		Chambre[] chambresDisponiblesTemp = new Chambre[listeChambres.length];
		Chambre[] chambresDisponibles = null; // liste des chambres disponibles
		int indiceChambreDispo = 0;
		boolean identique;
		boolean reservationMax = false;

		for (int i = 0; i < listeChambres.length; i++) { // parcours la liste des chambres de l'hotel

			if (!listeChambres[i].isReservePeriode(dateDebut, dateFin)) { // si la chambre n'est pas r�serv�

				identique = false;
				for (int j = 0; j < indiceChambreDispo; j++) { // on verifie que l'on a pas ajout� une chambre identique
					if (listeChambres[i].isIdentique(chambresDisponiblesTemp[j])) {
						identique = true;
					}
				}
				for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) { // on verifie que la chambre
																							// n'a pas atteint sa
																							// capacit� max
					reservationMax = true;
					if (listeChambres[i].getListeReservations()[j] == null) {
						reservationMax = false;
					}
				}

				if (!identique && !reservationMax) {// si on a pas une chambre identique et que la chambre n'a pas
													// atteint sa capacit� max, on l'ajoute � la liste de chambre
													// propos�es

					chambresDisponiblesTemp[indiceChambreDispo] = listeChambres[i];
					indiceChambreDispo++;

				}

			}
		}

		if (indiceChambreDispo > 0) { // si on a au moins une chambre � propos� on recr�e un tableau de la taille du
										// nombre de chambres propos�es
			chambresDisponibles = new Chambre[indiceChambreDispo - 1];
			for (int i = 0; i < chambresDisponibles.length; i++) {
				chambresDisponibles[i] = chambresDisponiblesTemp[i];
			}
		}

		return chambresDisponibles;
	}

	/**
	 * Methode qui permet d'afficher l' �tat de l'hotel
	 * 
	 * @param liste      chambres : represente toutes les chambres de l'hotel
	 * @param nom        : le nom du client
	 * @param superficie : repr�sente la superficie
	 * @param vue        : r�presente la(les) vue(s) de la chambre
	 * @param chambre    occup�e : savoir si une chambre est occup�e � la date
	 * @param liste      reservations : liste des r�servations
	 */
	public void afficherEtatHotel() {
		for (int i = 0; i < listeChambres.length; i++)
			listeChambres[i].afficherEtatChambre();
	}

	public void afficherReservationClient(String loginClient) {
		for (int i = 0; i < listeChambres.length; i++) {
			for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) {
				if (listeChambres[i].getListeReservations()[j] != null
						&& listeChambres[i].getListeReservations()[j].getClient().getLogin().equals(loginClient)) {
					System.out.println(
							"reservation de la " + listeChambres[i] + listeChambres[i].getListeReservations()[j]);
				}

			}
		}
	}

}
