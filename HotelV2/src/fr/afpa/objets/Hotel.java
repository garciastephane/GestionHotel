
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
	 * Constructeur Hotel, initialisation des chambres l'hotel, créeation d'un
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
	 * @return : un tableau d'Employe représentant la liste employe de l'hotel
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
		// différents reelement present dans l'hotel
		String[] listeLoginClient = new String[nbLogin];
		for (int i = 0; i < listeLoginClient.length; i++) {
			listeLoginClient[i] = listeLoginClientTemp[i];
		}

		return listeLoginClient;
	}

	/**
	 * retourne la liste les Client ayant au moins une reservation en cours dans
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

	/**
	 * methode de gestion de l'hotel (menu authentification)
	 */
	public void gestionHotel() {
		Scanner in = new Scanner(System.in);
		// on quitte le programme via le menu authentification (pour pouvoir
		// s'authenfier avec un client une fois cr��)
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
				listeReservationsClient(choix);
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
	 * Traitement du choix employe
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
			reservationChambre(employe[indiceEmploye], in);
			break;
		case "G":
			liberationChambre(employe[indiceEmploye], in);
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
				System.out.println("Le num�ro de la premi�re chambre libre est : " + listeChambres[i].getNumero());
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
				System.out.println("Le num�ro de la derni�re chambre libre est : " + listeChambres[i].getNumero());
				return;
			}
		}
	}

	/**
	 * retourne la liste des chambres disponibles lors du péroide et ayant moins de
	 * 5 reservations
	 * 
	 * @param dateDebut : date de debut de reservation
	 * @param dateFin   : date de fin de reservation
	 * @return
	 */
	public Chambre[] listeChambreDispo(LocalDate dateDebut, LocalDate dateFin) {
		Chambre[] chambresDisponiblesTemp = new Chambre[listeChambres.length];
		Chambre[] chambresDisponibles = null; // liste des chambres disponibles
		int indiceChambreDispo = 0;
		boolean identique;
		boolean reservationMax = false;

		for (int i = 0; i < listeChambres.length; i++) { // parcours la liste des chambres de l'hotel

			if (!listeChambres[i].isReservePeriode(dateDebut, dateFin)) { // si la chambre n'est pas r�serv�

				identique = false;
				for (int j = 0; j < indiceChambreDispo; j++) { // on verifie que l'on a pas ajout� une chambre
																// identique
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

		if (indiceChambreDispo > 0) { // si on a au moins une chambre � propos� on recr�e un tableau de la
										// taille du
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

	/**
	 * retourne la liste de reservation d'un client via son login
	 * @param loginClient : le login du client
	 * @return un tableau Reservation contenant la liste de reservation du client
	 */
	public Reservation[] listeReservationsClient(String loginClient) {
		//tableau de la taille max de reservation c'un client
		Reservation[] listeReservationsTemp = new Reservation[5];
		int nbReservation=0;
		
		//recuperation des reservations du client
		for (int i = 0; i < listeChambres.length; i++) {
			for (int j = 0; j < listeChambres[i].getListeReservations().length; j++) {
				if (listeChambres[i].getListeReservations()[j] != null
						&& listeChambres[i].getListeReservations()[j].getClient().getLogin().equals(loginClient)) {
					listeReservationsTemp[nbReservation]=listeChambres[i].getListeReservations()[j];
					nbReservation++;
				}

			}
		}
		//le client n'a pas de reservation
		if(nbReservation==0) {
			return null;
		}
		
		//tableau contenant le reservation du client (suppression des null)
		Reservation[] listeReservations = new Reservation[nbReservation];
		for (int i = 0; i < listeReservations.length; i++) {
			listeReservations[i]=listeReservationsTemp[i];
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
		Chambre[] chambresDisponible;
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
		chambresDisponible = listeChambreDispo(dateDebut, dateFin);
		if (chambresDisponible == null) {
			System.out.println("Désolé nous n'avons aucune chambre à vous proposer");
			return;
		}

		// affichage chambres disponibles
		for (i = 0; i < chambresDisponible.length; i++) {
			System.out.println(i + " : " + chambresDisponible[i]);
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
				chambresDisponible[indiceChambreReserve].getTarif());

		reservation.affichage(chambresDisponible[indiceChambreReserve]);
		System.out.println("Confirmation de la réservation de la chambre (oui ou non) ?");
		reponse = Saisie.saisieOuiouNon(in);

		if (reponse.equals("non")) {
			System.out.println("Désolé, que notre hotel ne vous convienne pas");
			return;
		}

		// ajout de la réservation à la liste Reservation de la chambre
		chambresDisponible[indiceChambreReserve].ajoutReservation(reservation);

		// +1 au nombre de réservations du client
		clientReservation.setNombreDeReservations(clientReservation.getNombreDeReservations() + 1);

		// payement de la reservation + alimentation fichier transactions
		reservation.payement(reservation.getMontantTotal(), in, cheminDossierTransaction);

		// envoie facture
		Facture.creationFacture(reservation, "nouvelle", employe, clientReservation);
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
		

		// authentification employé si echoue retour menu employé
		if (!Controle.authentificationEmploye(in, employe.getlogin(), cheminFichierMdp)) {
			System.out.println("Erreur authentification");
			return;
		}

		System.out.println("1 : libération en utilisant le  numéro de chambre. \n 2 : libération via nom client.");
		choix = Saisie.saisieChoixInt(in, 1, 2);
		if (choix == 1) { // liberation via le numero de chambre
			System.out.println("Veuillez entrer le numero de chambre : ");
			numeroChambre = Saisie.saisieChoixInt(in, 1, listeChambres.length);
			// si la chambre est occupee en ce moment on l'a libere
			if (listeChambres[numeroChambre - 1].isReserve(LocalDate.now())) {
				listeChambres[numeroChambre - 1].liberationChambre(in);
				return;
			} else { // sinon message erreur la chambre n'est pas occupée
				System.out.println("Cette chambre n'est pas occupée en ce moment");
				return;
			}
		} else {

			System.out.println("Veuillez entrer le nom du client : ");
			nomClient = Saisie.saisieAlphabetic(in);

			// verif si client existe recup login gestion 2 même nom
			for (int i = 0; i < getListeClients().length; i++) {
				if (getListeClients()[i].getNom().equals(nomClient)) {
					nbClientNom++;
					loginClient = getListeClients()[i].getLogin();
				}
			}
			if (nbClientNom == 0) { // aucun client avec ce nom existe
				System.out.println("Il n'y a aucun client avec ce nom dans notre hotel");
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
					System.out.println(i + " : " + clients[i].getLogin() + "nom : " + clients[i].getNom() + "prenom : "
							+ clients[i].getPrenom());
				}
				System.out.println("Votre choix  : ");

				// choix du client
				choix = Saisie.saisieChoixInt(in, 0, clients.length - 1);
				loginClient = clients[choix].getLogin();
			}

			
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
				System.out.println("Ce client n'occupe pas de chambre en ce moment");
				return;
			}

			// une seule chambre est occupée par le client, libération de cette chambre
			if (indiceChambreClient == 1) {
				chambresClient[0].liberationChambre(in);
				return;
			}

			// choix de la chambre à libérer
			System.out.println("Voici la liste des chambres que  le client occupe en ce moment  : ");
			for (int i = 0; i < indiceChambreClient; i++) {
				System.out.println(i + " : " + chambresClient[i].getNumero());
			}
			System.out.println("Entrer votre choix :  ");
			chambresClient[Saisie.saisieChoixInt(in, 0, indiceChambreClient - 1)].liberationChambre(in);
			
		
		}
	}
			
			/** methode qui permets l'annulation d'une reservation
			 * @param numeroReservation   : num�ro de la r�servation � modifier
			 * @param listeReservations   : liste de r�servations du client
			 * @param loginClient         : login du client
			 * @param in                  : Le scanner pour la saisie d'informations
		    */
		
						
			public void annulationReservation() {
				
				
				/*// authentification employ� si echoue retour menu employ�
				Scanner in;
				if (!Controle.authentificationEmploye(in, employe.getlogin(), cheminFichierMdp)) {
					System.out.println("Erreur authentification");
					return;
									
					}	
				//affiche la liste des clients
			System.out.println("Voici la liste des clients de l'hotel : ");
			for (int i = 0; i < listeClients.length; i++) {
				//System.out.println(listeClients[i]);
				
			System.out.println("Veuillez confirmez votre num�ro de client");
				for(int j = 0; j <listeLoginClient.length; j++) {
					loginClient = Saisie.saisieLoginExistant( hotel.listeLoginClient());
					clientReservation=hotel.clientReservation(loginClient);
					
						if (listeReservationsClient[i] != null && listeReservationsClient[i].getDateDebut().getDateFin()) {
					return;
					System.out.println("Vos r�servations");
					System.out.println("Quelle r�servation souhaitez vous annuler");
					if{
						
					}
					
				}*/
				
			}		
		
	}
		

