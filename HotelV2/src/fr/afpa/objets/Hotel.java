
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
	private String cheminDossierFacture;
	String loginClient = "";

	/**
	 * Constructeur Hotel, initialisation des chambres l'hotel, crÃ©eation d'un
	 * employe admin et gestion de hotel
	 * 
	 * @param nom_ : nom de l'hotel
	 */
	public Hotel(String nom_) {
		nom = nom_;
		cheminDossierTransaction = "ressources\\transactions\\";
		cheminFichierMdp = "ressources\\mdp.txt";
		cheminDossierFacture = "ressources\\facture_pdf\\";
		// initialisation listeChambres via ficier csv
		creationHotel("ressources\\ListeChambres_V3.csv");

		// creation employe admin rÃ©intialisation des employe Ã  chaque demarage du
		// programme
		employe = new Employe[1];
		employe[0] = new Employe("GH000", "nom admin", "prenom admin");

		// ecriture login/mot de passe de l'employe dans un fichier texte
		Fichier.ecritureFichier(cheminFichierMdp, "fonction;login;mdp;mdpAdmin", false);
		Fichier.ecritureFichier(cheminFichierMdp, "employe;" + "GH000" + ";" + "admin" + ";" + "admin", true);

		// appel methode de gestion de l'hotel;
		gestionHotel();
	}

	/**
	 * modifie la valeur de listeChambres
	 * 
	 * @param listeChambres_ : la nouvelle liste de Chambre
	 */
	public void setListeChambres(Chambre[] listeChambres_) {
		listeChambres = listeChambres_;

	}

	/**
	 * retourne la liste des chambres
	 * 
	 * @return un tableau de Chambre represantant la liste de chambres de l'hotel
	 */
	public Chambre[] getListeChambres() {
		return listeChambres;
	}

	/**
	 * modifie le nom de l'hotel
	 * 
	 * @param nom_ : le nouveau nom de l'hotel
	 */
	public void setNom(String nom_) {
		nom = nom_;
	}

	/**
	 * retourne le nom de l'hotel
	 * 
	 * @return une chaine de caracteres representant le nom de l'hotel
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * modifie la liste Employe de l'hotel
	 * 
	 * @param employe_ : la nouvelle liste employe
	 */
	public void setEmploye(Employe[] employe_) {
		employe = employe_;
	}

	/**
	 * retourne la liste des employes
	 * 
	 * @return : un tableau d'Employe reprÃ©sentant la liste employe de l'hotel
	 */
	public Employe[] getEmploye() {
		return employe;
	}

	/**
	 * modifie le chemin du dossier des trasactions
	 * 
	 * @param cheminDossierTransaction_ : le nouveau chemin du dossier transactions
	 */
	public void setCheminDossierTransaction(String cheminDossierTransaction_) {
		cheminDossierTransaction = cheminDossierTransaction_;
	}

	/**
	 * retourne le chemin du dossier des trasactions
	 * 
	 * @return une chaine de caracteres representant le chemin du dossier des
	 *         trasactions
	 */
	public String getCheminDossierTransaction() {
		return cheminDossierTransaction;
	}

	/**
	 * modifie le chemin du fichier d'authetifications
	 * 
	 * @param cheminFichierMdp_ le nouveau chemin du fichier d'authetifications
	 */
	public void setCheminFichierMdp(String cheminFichierMdp_) {
		cheminFichierMdp = cheminFichierMdp_;
	}

	/**
	 * retourne le chemin du fichier d'authetifications
	 * 
	 * @return une chaine de caracteres representant le chemin du fichier
	 *         d'authetifications
	 */
	public String getCheminFichierMdp() {
		return cheminFichierMdp;
	}

	/**
	 * retourne la liste des logins des clients ayant au moins une reservation en
	 * cours
	 * 
	 * @return : un tableau de chaine de caracteres representant la liste des login
	 *         client ayant au moins une reservation dans l'hotel
	 */
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
		// diffÃ©rents reelement present dans l'hotel
		String[] listeLoginClient = new String[nbLogin];
		for (int i = 0; i < listeLoginClient.length; i++) {
			listeLoginClient[i] = listeLoginClientTemp[i];
		}

		return listeLoginClient;
	}

	/**
	 * retourne la liste des Clients ayant au moins une reservation en cours dans
	 * l'hotel
	 * 
	 * @return un tableau de Client ayant au moins une reservation en cours dans
	 *         l'hotel
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

	/**
	 * 
	 * @param loginClient
	 * @return
	 */
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

	/**
	 * lecture du fichier et crï¿½ation des chambres de l'hotel
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

		// calcul du nombre de chambres ï¿½ crï¿½er
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
				// nombre de chambres ï¿½ crï¿½er pour chaque type
				nbChambres = Integer.parseInt(ligne[5]);
				for (int j = 0; j < nbChambres; j++) {
					listeChambres[indexChambre] = new Chambre((indexChambre + 1), ligne[0], ligne[1], ligne[2],
							Integer.parseInt(ligne[4]), ligne[6].split("\\|"));
					indexChambre++;
				}
			}
		}

	}

	/**
	 * methode de gestion de l'hotel (menu authentification)
	 */
	public void gestionHotel() {
		Scanner in = new Scanner(System.in);
		// on quitte le programme via le menu authentification (pour pouvoir
		// s'authenfier avec un client une fois crï¿½ï¿½)
		while (menuAuthentification(in))
			;

	}

	/**
	 * menu authentification (authentification(employe ou client), creer un employe,
	 * quitter le programme
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 * @return false si l'utiliateur decide de quitter, false sinon
	 */
	public boolean menuAuthentification(Scanner in) {

		System.out.print("tapez 1 si vous voulez creer un nouvel employe, tapez \"Q\" pour quitter \nLogin : ");
		String choix = in.nextLine();

		if (choix.equals("1")) { // creation employe
			creationEmploye(in);
			return true;
		}

		if (choix.equals("Q")) { // quitter programme
			return false;
		}

		if (Controle.isNumerique(choix, 10)) { // authentification d'un client

			if (!Controle.isUnique(choix, listeLoginClient())) { // authentification reussit (client existant)
				System.out.println("\n");
				affichageListeReservationsClient(choix);
			} else { // authentification mauvaise (client non existant)
				System.out.println("Erreur authentification client");
			}
			return true;
		}

		if (Controle.isAlphaNumerique(choix, 5)) { // authentification employe

			if (Controle.authentificationEmploye(in, choix, cheminFichierMdp)) { // authentification via mot de passe
				int indiceEmploye = 0;
				for (int i = 0; i < employe.length; i++) {
					if (employe[i].getlogin().equals(choix)) {
						indiceEmploye = i;
						break;
					}
				}

				affichageMenu(); // authentification reussit
				while (choixEmploye(in, indiceEmploye)) { // affichage menu employe jusqu' a ce qu'il decide de quitter
					affichageMenu();
				}
			} else {
				System.out.println("Erreur d'authentification"); // authentification mauvaise(login ou mot de passe non
																	// valide)
			}
			return true;
		}
		System.out.println("ERREUR");
		return true;

	}

	/**
	 * methode de crï¿½ation de l'employï¿½ de l'hotel
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 */
	private void creationEmploye(Scanner in) {
		String login;
		String motDePasse;
		String nom;
		String prenom;

		// Demande des informations pour la crï¿½ation de l'employï¿½
		System.out.println("Entrer le login de l'employe : ");
		login = Saisie.saisieLoginEmploye(in);
		System.out.println("Entrer le mot de passe de l'employe : ");
		motDePasse = Saisie.saisieNonVide(in);
		System.out.println("Entrer le nom de l'employe : ");
		nom = Saisie.saisieAlphabetic(in);
		System.out.println("Entrer le prenom de l'employe : ");
		prenom = Saisie.saisieAlphabetic(in);

		// ajout de l'employe dans la liste employe
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
		// ajout du login et mot de passe de l'employe au fichier mot de passe
		Fichier.ecritureFichier(cheminFichierMdp, "employe;" + login + ";" + motDePasse + ";" + motDePasse, true);

	}

	/**
	 * Affichage Menu employe
	 */
	public void affichageMenu() {
		System.out.println("---------------------   MENU HOTEL CDA JAVA  ------------------");
		System.out.println("");
		System.out.println("A- Afficher l'ï¿½tat de l'hï¿½tel");
		System.out.println("B- Afficher le nombre de chambres rï¿½servï¿½es");
		System.out.println("C- Afficher le numï¿½ro de chambres libres");
		System.out.println("D- Afficher le numï¿½ro de la premiï¿½re chambre vide");
		System.out.println("E- Afficher le numï¿½ro de la derniï¿½re chambre vide");
		System.out.println("F- Rï¿½server une chambre");
		System.out.println("G- Libï¿½rer une chambre");
		System.out.println("H- Modifier une rï¿½servation");
		System.out.println("I- Annuler une rï¿½servation");
		System.out.println("Q- Quitter");
		System.out.println("");
		System.out.println("---------------------------------------------------------------");
		System.out.println("Votre choix :");
	}

	/**
	 * Traitement du choix employe
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 * @return : false si l'employï¿½ veut quitter le Menu, true sinon
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
			reservationChambre(employe[indiceEmploye], in);
			break;
		case "G":
			liberationChambre(employe[indiceEmploye], in);
			break;
		case "H":
			modificationReservation(employe[indiceEmploye], in);
			break;
		case "I":
			annulationReservation(employe[indiceEmploye], in);
			break;
		case "Q":
			return false;
		default:
			System.out.println("Veuillez entrer un choix valide");
		}
		return true;

	}

	/**
	 * Affiche le nombre de chambres occupees en ce moment
	 */
	public void afficherNombreChambresReserves() {
		int nombreChambresReserves = 0;
		for (int i = 0; i < listeChambres.length; i++) {

			if (listeChambres[i].isReserve(LocalDate.now())) {
				nombreChambresReserves++;
			}

		}

		System.out.println("il y a " + nombreChambresReserves + " chambres reservees");

	}

	/**
	 * Affiche le nombre de chambres libres en ce moment
	 */
	public void afficherNombreChambresLibres() {
		int nombreChambresLibres = 0;
		for (int i = 0; i < listeChambres.length; i++) {

			if (!listeChambres[i].isReserve(LocalDate.now())) {
				nombreChambresLibres++;
			}
		}

		System.out.println("il y a " + nombreChambresLibres + " chambres libres");

	}

	/**
	 * Affiche le numero de la premiere chambre libre
	 */
	public void afficherNumeroPremiereChambreLibre() {

		for (int i = 0; i < listeChambres.length; i++) {

			if (!listeChambres[i].isReserve(LocalDate.now())) {
				System.out.println("Le numï¿½ro de la premiï¿½re chambre libre est : " + listeChambres[i].getNumero());
				return;
			}
		}

	}

	/**
	 * affiche le numero de la derniere chambre libre
	 */
	public void afficherNumeroDerniereChambreLibre() {

		for (int i = listeChambres.length - 1; i >= 0; i--) {
			if (!listeChambres[i].isReserve(LocalDate.now())) {
				System.out.println("Le numï¿½ro de la derniï¿½re chambre libre est : " + listeChambres[i].getNumero());
				return;
			}
		}
	}

	/**
	 * retourne la liste des indices des chambres disponibles lors de la période et
	 * ayant moins de 5 reservations
	 * 
	 * @param dateDebut : date de debut de reservation
	 * @param dateFin   : date de fin de reservation
	 * @return
	 */
	public int[] indiceListeChambreDispo(LocalDate dateDebut, LocalDate dateFin) {
		int[] indiceChambresDisponiblesTemp = new int[listeChambres.length];
		int[] indiceChambresDisponibles = null; // liste des chambres disponibles
		int indiceChambreDispoMax = 0;
		boolean identique;
		boolean reservationMax = false;

		for (int indiceChambreHotel = 0; indiceChambreHotel < listeChambres.length; indiceChambreHotel++) { // parcours
																											// la liste
																											// des
																											// chambres
																											// de
																											// l'hotel

			if (!listeChambres[indiceChambreHotel].isReservePeriode(dateDebut, dateFin)) { // si la chambre n'est pas
																							// r�serv�

				identique = false;
				for (int indiceChambreDispo = 0; indiceChambreDispo < indiceChambreDispoMax; indiceChambreDispo++) { // on
																														// verifie
																														// que
																														// l'on
																														// a
																														// pas
																														// ajout�
																														// une
																														// chambre
					// identique
					if (listeChambres[indiceChambreHotel]
							.isIdentique(listeChambres[indiceChambresDisponiblesTemp[indiceChambreDispo]])) {
						identique = true;
					}
				}
				for (int j = 0; j < listeChambres[indiceChambreHotel].getListeReservations().length; j++) { // on
																											// verifie
																											// que la
																											// chambre
					// n'a pas atteint sa
					// capacite max
					reservationMax = true;
					if (listeChambres[indiceChambreHotel].getListeReservations()[j] == null) {
						reservationMax = false;
						break;
					}
				}

				if (!identique && !reservationMax) {// si on a pas une chambre identique et que la chambre n'a pas
													// atteint sa capacit� max, on l'ajoute � la liste de chambre
													// propos�es

					indiceChambresDisponiblesTemp[indiceChambreDispoMax] = indiceChambreHotel;
					indiceChambreDispoMax++;

				}

			}
		}

		if (indiceChambreDispoMax > 0) { // si on a au moins une chambre � propos� on recr�e un tableau de la
											// taille du
											// nombre de chambres propos�es
			indiceChambresDisponibles = new int[indiceChambreDispoMax];
			for (int i = 0; i < indiceChambresDisponibles.length; i++) {
				indiceChambresDisponibles[i] = indiceChambresDisponiblesTemp[i];
			}
		}

		return indiceChambresDisponibles;
	}

	/**
	 * Methode qui permet d'afficher l' ï¿½tat de l'hotel
	 * 
	 * @param liste      chambres : represente toutes les chambres de l'hotel
	 * @param nom        : le nom du client
	 * @param superficie : reprï¿½sente la superficie
	 * @param vue        : rï¿½presente la(les) vue(s) de la chambre
	 * @param chambre    occupï¿½e : savoir si une chambre est occupï¿½e ï¿½ la date
	 * @param liste      reservations : liste des rï¿½servations
	 */
	public void afficherEtatHotel() {
		System.out.println("Le chiffre d'affaires de la journée est de : " + calculCA());

		for (int i = 0; i < listeChambres.length; i++)
			listeChambres[i].afficherEtatChambre();
	}

	/**
	 * retourne la liste de reservation d'un client via son login
	 * 
	 * @param loginClient : le login du client
	 * @return un tableau Reservation contenant la liste de reservation du client
	 */
	public Reservation[] listeReservationsClient(String loginClient) {
		// tableau de la taille max de reservation c'un client
		Reservation[] listeReservationsTemp = new Reservation[5];
		int nbReservation = 0;

		// recuperation des reservations du client
		for (int i = 0; i < listeChambres.length; i++) {
			for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) {
				if (listeChambres[i].getListeReservations()[j] != null
						&& listeChambres[i].getListeReservations()[j].getClient().getLogin().equals(loginClient)) {
					listeReservationsTemp[nbReservation] = listeChambres[i].getListeReservations()[j];
					nbReservation++;
				}

			}
		}
		// le client n'a pas de reservation
		if (nbReservation == 0) {
			return null;
		}

		// tableau contenant le reservation du client (suppression des null)
		Reservation[] listeReservations = new Reservation[nbReservation];
		for (int i = 0; i < listeReservations.length; i++) {
			listeReservations[i] = listeReservationsTemp[i];
		}
		return listeReservations;
	}

	/**
	 * mehtode de reservation d'une chambre
	 * 
	 * @param employe : l'employe effectuant la reservation
	 * @param in      : Le scanner pour la saisie d'informations
	 */
	public void reservationChambre(Employe employe, Scanner in) {
		String reponse = "";
		String loginClient = "";
		LocalDate dateDebut;
		LocalDate dateFin;
		int indiceChambreReserve = 0;
		int[] indiceChambresDisponible;
		Client clientReservation = null;
		int i;

		// authentification employé si echoue retour menu employé
		if (!Controle.authentificationEmploye(in, employe.getlogin(), cheminFichierMdp)) {
			System.out.println("Erreur authentification");
			return;
		}

		if (listeLoginClient()[0].equals("")) { // l'hotel n'a pas de clients
			System.out.println("il n'y a pas encore de client dans l'hotel");
			clientReservation = employe.creationNouveauClient(in, listeLoginClient());
		} else {
			System.out.println("Etes vous un nouveau client ? (oui ou non)");
			if (Saisie.saisieOuiouNon(in).equals("oui")) { // réservation d'un nouveau client
				clientReservation = employe.creationNouveauClient(in, listeLoginClient());
			} else { // reservation d'un client fidèle à l'hotel
				System.out.println("Voici la liste des clients de l'hotel : ");
				afficherListeClient();
				System.out.println("Veuillez entrer le login du client");
				loginClient = Saisie.saisieLoginExistant(in, listeLoginClient());
				clientReservation = clientReservation(loginClient);

			}
		}

		// verification du nombre de réservation du client
		if (clientReservation.getNombreDeReservations() >= 5) {
			System.out.println("Désolé, vous avez déjà 5 réservations dans notre hotel");
			return;
		}

		// demande des dates de séjour valide
		System.out.println("quel est votre date de début de séjour dans notre hotel (format dd/MM/yyyy)");
		dateDebut = LocalDate.parse(Saisie.saisieDate(in, LocalDate.now()), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		System.out.println("quel est votre date de fin du séjour dans notre hotel");
		dateFin = LocalDate.parse(Saisie.saisieDate(in, dateDebut.plusDays(1)),
				DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		// recherche des chambres disponibles aux dates demandées et si il n'y a pas
		// déjà 5 reservations dans la chambres (capacité?)
		indiceChambresDisponible = indiceListeChambreDispo(dateDebut, dateFin);
		if (indiceChambresDisponible == null) {
			System.out.println("Désolé nous n'avons aucune chambre à vous proposer");
			return;
		}

		// affichage chambres disponibles
		for (i = 0; i < indiceChambresDisponible.length; i++) {
			System.out.println(i + " : " + listeChambres[indiceChambresDisponible[i]]);
		}
		System.out.println(i + " : aucune chambres proposées vous convient, annulation de la réservation");

		// choix utilisateur
		System.out.println("quelle chambre voulez-vous reserver ? (choix entre 0 et " + i);
		indiceChambreReserve = Saisie.saisieChoixInt(in, 0, i);
		if (indiceChambreReserve == i) {
			System.out.println("Désolé que aucune chambre vous convienne");
			return;
		}

		// creation de la reservation
		Reservation reservation = new Reservation(dateDebut, dateFin, clientReservation,
				listeChambres[indiceChambresDisponible[indiceChambreReserve]].getTarif());

		reservation.affichage(listeChambres[indiceChambresDisponible[indiceChambreReserve]]);
		System.out.println("Confirmation de la réservation de la chambre (oui ou non) ?");
		reponse = Saisie.saisieOuiouNon(in);

		if (reponse.equals("non")) {
			System.out.println("Désolé, que notre hotel ne vous convienne pas");
			return;
		}

		// ajout de la réservation à la liste Reservation de la chambre
		listeChambres[indiceChambresDisponible[indiceChambreReserve]].ajoutReservation(reservation);

		// +1 au nombre de réservations du client
		reservation.getClient().setNombreDeReservations(reservation.getClient().getNombreDeReservations() + 1);

		// payement de la reservation + alimentation fichier transactions
		reservation.payement(reservation.getMontantTotal(), in, cheminDossierTransaction);

		// envoie facture
		String cheminFacture = Facture.creationFacture(reservation, employe,
				listeChambres[indiceChambresDisponible[indiceChambreReserve]], cheminDossierFacture);
		// Mail.envoiMail(cheminFacture, reservation.getClient().getMail());
	}

	/**
	 * methode de liberation d'une chambre
	 * 
	 * @param in    : Le Scanner pour la saisie utilisateur
	 * @param hotel :
	 */
	public void liberationChambre(Employe employe, Scanner in) {
		int choix;
		int numeroChambre;
		String nomClient;
		int nbClientNom = 0;
		String loginClient = "";
		Chambre[] chambresClient = new Chambre[5];
		int indiceChambreClient = 0;
		int indiceChambreChoisie = 0;

		// authentification employé si echoue retour menu employé
		if (!Controle.authentificationEmploye(in, employe.getlogin(), cheminFichierMdp)) {
			System.out.println("Erreur authentification");
			return;
		}

		System.out.println(
				"1 : libération en utilisant le  numéro de chambre. \n 2 : libération en utilisant le nom du client.");
		choix = Saisie.saisieChoixInt(in, 1, 2);
		if (choix == 1) { // liberation via le numero de chambre
			System.out.println("Veuillez entrer le numero de la chambre (entre 1 et " + listeChambres.length +" ) :");
			numeroChambre = Saisie.saisieChoixInt(in, 1, listeChambres.length);
			// si la chambre est occupee en ce moment on l'a libere
			if (listeChambres[numeroChambre - 1].isReserve(LocalDate.now())) {
				listeChambres[numeroChambre - 1].liberationChambre(in);
				return;
			} else { // sinon message erreur la chambre n'est pas occupée
				System.out.println("ERREUR : Cette chambre n'est pas occupée en ce moment.");
				return;
			}
		} else { // liberation via le nom client

			System.out.println("Veuillez entrer le nom du client : ");
			nomClient = Saisie.saisieAlphabetic(in);

			// verif si client existe et recuperation du nombre de client ayant ce nom
			for (int i = 0; i < getListeClients().length; i++) {
				if (getListeClients()[i].getNom().equals(nomClient)) {
					nbClientNom++;
					loginClient = getListeClients()[i].getLogin();
				}
			}
			if (nbClientNom == 0) { // aucun client avec ce nom existe
				System.out.println("ERREUR : Il n'y a aucun client avec ce nom dans notre hotel");
				return;
			}
			if (nbClientNom > 1) { // plus de un client à le même nom

				Client[] clients = new Client[nbClientNom]; // liste de clients ayant le meme nom
				nbClientNom = 0;
				// recuperation des clients ayant le nom recherche
				for (int i = 0; i < getListeClients().length; i++) {
					if (getListeClients()[i].getNom().equals(nomClient)) {
						clients[nbClientNom] = getListeClients()[i];
						nbClientNom++;
					}
				}
				System.out.println(
						"Il y a plusieurs clients avec le nom demandé. Veuillez choisir le client correspondant");
				for (int i = 0; i < clients.length; i++) {
					System.out.println(i + " : login : " + clients[i].getLogin() + " nom : " + clients[i].getNom()
							+ " prenom : " + clients[i].getPrenom());
				}
				System.out.println("Votre choix  : ");

				// choix du client
				choix = Saisie.saisieChoixInt(in, 0, clients.length - 1);
				loginClient = clients[choix].getLogin();
			}

			// Recherche des chambres que le client occupe en ce monent
			// parcours de la liste des chambres de l'hotel
			for (int indiceChambre = 0; indiceChambre < listeChambres.length; indiceChambre++) {

				// si la chambre est occupé en ce moment
				if (listeChambres[indiceChambre].isReserve(LocalDate.now())) {

					// parcours des reservations de la chambre
					for (int indiceReserv = 0; indiceReserv < listeChambres[indiceChambre]
							.getListeReservations().length; indiceReserv++) {

						// si la reservation est en cours et elle apartient au client ajout a la liste
						// des chambres du client suseptible d'être libéré
						if (listeChambres[indiceChambre].getListeReservations()[indiceReserv] != null
								&& listeChambres[indiceChambre].getListeReservations()[indiceReserv].isEnCours()
								&& listeChambres[indiceChambre].getListeReservations()[indiceReserv].getClient()
										.getLogin().equals(loginClient)) {
							chambresClient[indiceChambreClient] = listeChambres[indiceChambre];
							indiceChambreClient++;
						}
					}
				}

			}

			// aucune chambre n'est occupée en ce moment par le client
			if (indiceChambreClient == 0) {
				System.out.println("ERREUR : Ce client n'occupe pas de chambre en ce moment.");
				return;
			}

			// une seule chambre est occupée par le client, libération de cette chambre
			if (indiceChambreClient == 1) {
				for (int i = 0; i < listeChambres.length; i++) {
					if (chambresClient[0].getNumero() == listeChambres[i].getNumero()) {
						listeChambres[i].liberationChambre(in);
						return;
					}
				}
			}

			// choix de la chambre à libérer
			System.out.println("Voici la liste des chambres que  le client occupe en ce moment  : ");
			for (int i = 0; i < indiceChambreClient; i++) {
				System.out.println(i + " : numero : " + chambresClient[i].getNumero() + " type : "
						+ chambresClient[i].getTypeDeChambre());
			}
			System.out.println("Entrer votre choix :  ");

			indiceChambreChoisie = Saisie.saisieChoixInt(in, 0, indiceChambreClient - 1);
			for (int i = 0; i < listeChambres.length; i++) {
				if (chambresClient[indiceChambreChoisie].getNumero() == listeChambres[i].getNumero()) {
					listeChambres[i].liberationChambre(in);
					return;
				}
			}

		}
	}

	public void modificationReservation(Employe employe, Scanner in) {
		String loginClient;
		int choix;
		int numeroReservationModifier;
		LocalDate dateDebut;
		LocalDate dateFin;
		int indiceChambre = 0;
		int ancienPrix = 0;

		// authentification employï¿½ si echoue retour menu employï¿½

		if (!Controle.authentificationEmploye(in, employe.getlogin(), cheminFichierMdp)) {
			System.out.println("Erreur authentification");
			return;
		}
		if (getListeClients() == null) {
			System.out.println("Il y a aucunes rï¿½servations ï¿½ modifier");
			return;
		}
		// affiche la liste des clients
		System.out.println("La liste des clients de l'hotel : ");
		Client[] listeClients = getListeClients();
		for (int i = 0; i < listeClients.length; i++) {
			System.out.println(listeClients[i]);
		}

		System.out.println("Veuillez entrer le login du client qui souhaite modifier sa rï¿½servation");
		loginClient = Saisie.saisieLoginExistant(in, listeLoginClient());
		Reservation[] listeReservationClients = listeReservationsClient(loginClient);
		for (int k = 0; k < listeReservationClients.length; k++) {
			System.out.println(k + " : " + listeReservationClients[k]);
		}
		System.out.println("Veuillez entrer votre choix : ");
		choix = Saisie.saisieChoixInt(in, 0, listeReservationClients.length - 1);
		numeroReservationModifier = listeReservationClients[choix].getNumeroReservation();

		for (int i = 0; i < listeChambres.length; i++) {
			for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) {
				if (listeChambres[i].getListeReservations()[j] != null && listeChambres[i].getListeReservations()[j]
						.getNumeroReservation() == numeroReservationModifier) {
					indiceChambre = i;
					break;
				}
			}
		}

		// demande des dates de sÃ©jour valide
		System.out.println("quel est votre date de dÃ©but de sÃ©jour dans notre hotel (format dd/MM/yyyy)");
		dateDebut = LocalDate.parse(Saisie.saisieDate(in, LocalDate.now()), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		System.out.println("quel est votre date de fin du sÃ©jour dans notre hotel");
		dateFin = LocalDate.parse(Saisie.saisieDate(in, dateDebut.plusDays(1)),
				DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		for (int i = 0; i < listeChambres[indiceChambre].getListeReservations().length; i++) {
			if (listeChambres[indiceChambre].getListeReservations()[i] != null
					&& listeChambres[indiceChambre].getListeReservations()[i]
							.getNumeroReservation() != numeroReservationModifier) {

				if (dateDebut.compareTo(listeChambres[indiceChambre].getListeReservations()[i].getDateDebut()) >= 0) {
					// Vï¿½rifier si modifier rï¿½servation est possible
				}
			}
		}

		for (int j = 0; j < listeChambres[indiceChambre].getListeReservations().length; j++) {
			if (listeChambres[indiceChambre].getListeReservations()[j] != null
					&& listeChambres[indiceChambre].getListeReservations()[j]
							.getNumeroReservation() == numeroReservationModifier) {
				ancienPrix = listeChambres[indiceChambre].getListeReservations()[j]
						.calculMontant(listeChambres[indiceChambre].getTarif());
				listeChambres[indiceChambre].getListeReservations()[j]
						.modifReservationPayement(listeChambres[indiceChambre].getTarif(), dateDebut, dateFin, in);
				String cheminFacture = Facture.modificationFacture(
						listeChambres[indiceChambre].getListeReservations()[j], employe, listeChambres[indiceChambre],
						cheminDossierFacture, ancienPrix);
				Mail.envoiMail(cheminFacture,
						listeChambres[indiceChambre].getListeReservations()[j].getClient().getMail());
				return;
			}
		}

	}

	public void annulationReservation(Employe employe, Scanner in) {

		String loginClient;
		int choix;
		int numeroReservationModifier;
		int indiceChambre = 0;

		// authentification employï¿½ si echoue retour menu employï¿½

		if (!Controle.authentificationEmploye(in, employe.getlogin(), cheminFichierMdp)) {
			System.out.println("Erreur authentification");
			return;
		}

		if (getListeClients() == null) {
			System.out.println("Il y a aucunes rï¿½servations ï¿½ annuler");
			return;
		}
		// affiche la liste des clients
		System.out.println("La liste des clients de l'hotel : ");
		Client[] listeClients = getListeClients();
		for (int i = 0; i < listeClients.length; i++) {
			System.out.println(listeClients[i]);
		}

		System.out.println("Veuillez entrer le login du client qui souhaite modifier sa rï¿½servation");
		loginClient = Saisie.saisieLoginExistant(in, listeLoginClient());
		Reservation[] listeReservationClients = listeReservationsClient(loginClient);
		for (int k = 0; k < listeReservationClients.length; k++) {
			System.out.println(k + " : " + listeReservationClients[k]);
		}
		System.out.println("Veuillez entrer votre choix : ");
		choix = Saisie.saisieChoixInt(in, 0, listeReservationClients.length - 1);
		numeroReservationModifier = listeReservationClients[choix].getNumeroReservation();

		for (int i = 0; i < listeChambres.length; i++) {
			for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) {
				if (listeChambres[i].getListeReservations()[j] != null && listeChambres[i].getListeReservations()[j]
						.getNumeroReservation() == numeroReservationModifier) {
					indiceChambre = i;
					break;
				}
			}
		}

		for (int j = 0; j < listeChambres[indiceChambre].getListeReservations().length; j++) {
			if (listeChambres[indiceChambre].getListeReservations()[j] != null
					&& listeChambres[indiceChambre].getListeReservations()[j]
							.getNumeroReservation() == numeroReservationModifier) {
				if (listeChambres[indiceChambre].getListeReservations()[j].getDateDebut().isBefore(LocalDate.now())) {
					System.out.println("La reservation est en cours, veuillez passer par le service de liberation");
					return;
				}
				listeChambres[indiceChambre].getListeReservations()[j].modifReservationPayement(
						listeChambres[indiceChambre].getTarif(), LocalDate.now(), LocalDate.now(), in);
				listeChambres[indiceChambre].getListeReservations()[j] = null;
				return;
			}
		}
	}

	/**
	 * service qui permet de calculer le chiffres d'affaires de la journee
	 * 
	 * @return : un entier representant le chiffre d'affaires
	 */
	public int calculCA() {
		int chiffreAffaires = 0;
		String cheminFichier = cheminDossierTransaction + "transactions"
				+ LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".txt";
		String listeTransactions[] = Fichier.lecture(cheminFichier);
		String[] transaction;
		for (int i = 1; i < listeTransactions.length; i++) {
			transaction = listeTransactions[i].split(";");
			if (transaction.length>1 && transaction[1].equals("remboursement")) {
				chiffreAffaires -= Integer.parseInt(transaction[2]);
			} else if (transaction.length>1 && transaction[1].equals("payement")) {
				chiffreAffaires += Integer.parseInt(transaction[2]);
			}
		}
		return chiffreAffaires;

	}
	
	/**
	 * retourne la liste de reservation d'un client via son login
	 * 
	 * @param loginClient : le login du client
	 * @return un tableau Reservation contenant la liste de reservation du client
	 */
	public void affichageListeReservationsClient(String loginClient) {
	
		// recuperation des reservations du client
		for (int i = 0; i < listeChambres.length; i++) {
			for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) {
				if (listeChambres[i].getListeReservations()[j] != null
						&& listeChambres[i].getListeReservations()[j].getClient().getLogin().equals(loginClient)) {
				System.out.println("- Reservation n° " + listeChambres[i].getListeReservations()[j].getNumeroReservation() + " ,chambre n°" + listeChambres[i].getNumero() +  " ,type : " + listeChambres[i].getTypeDeChambre());
				System.out.println("Periode de reservation : du " + listeChambres[i].getListeReservations()[j].getDateDebut() + " au " + listeChambres[i].getListeReservations()[j].getDateFin() );
				System.out.println("---------------------------------------------------------------");
				}

			}
			
		}
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
