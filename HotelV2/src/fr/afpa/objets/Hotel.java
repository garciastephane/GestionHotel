
package fr.afpa.objets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Hotel {
	private Chambre[] listeChambres;
	private String nom;
	private Client[] listeClients;
	private Employe employe;
	private String cheminDossierTransaction; // chemin dossier puis ajout du fichier de lors de l'ecriture de la
												// transaction dans le fichier du jour
	private String cheminFichierMdp;

	public Hotel(String nom_) {
		nom = nom_;
		cheminDossierTransaction = "ressources\\transactions\\";
		cheminFichierMdp = "ressources\\mdp.txt";
		// initialisation listeChambres via ficier csv
		creationHotel("ressources\\ListeChambres_V3.csv");

		//creation employe admin
		employe=new Employe("GH000", "nom admin", "prenom admin");
		
		//ecriture login/mot de passe de l'employe dans un fichier texte
		Fichier.ecritureFichier(cheminFichierMdp, "fonction;login;mdp;mdpAdmin", false);
		Fichier.ecritureFichier(cheminFichierMdp, "employe;" + "GH000" + ";" + "admin"+ ";" + "admin", true);
		listeClients = null;
		

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

	public void setListeClients(Client[] listeClients_) {
		listeClients = listeClients_;

	}

	public Client[] getListeClients() {
		return listeClients;
	}

	public void setEmploye(Employe employe_) {
		employe = employe_;
	}

	public Employe getEmploye() {
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
	 * lecture du fichier et création des chambres de l'hotel
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

		// calcul du nombre de chambres à créer
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
				// nombre de chambres à créer pour chaque type
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
		// s'authenfier avec un client une fois créé)
		while (menuAuthentification(in))
			;

	}

	/**
	 * menu authentification (authentification(employé ou client), créé un employé,
	 * quitter le programme
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 * @return false si l'utiliateur décide de quitter, false sinon
	 */
	public boolean menuAuthentification(Scanner in) {

		System.out.print("tapez 1 si vous voulez créer un nouvel employé, tapez \"Q\" pour quitter \nLogin : ");
		String choix = in.nextLine();

		if (choix.equals("1")) { // creation employé
			creationEmploye(in);
			return true;
		}

		if (choix.equals("Q")) { // quitter programme
			return false;
		}

		if (Controle.isNumerique(choix, 10)) { // authentification d'un client

			if (!Controle.isUnique(choix, listeLoginClient())) { // authentification réussit (client existant)

				for (int i = 0; i < listeClients.length; i++) { // recupération du client dans la liste client

					if (listeClients[i].getLogin().equals(choix)) {
						listeClients[i].afficherReservations(); // affiche ces réservations ou message erreur
					}
				}
			} else { // authentification mauvaise (client non existant)
				System.out.println("Erreur authentification client");
			}
			return true;
		}

		if (Controle.isAlphaNumerique(choix, 5)) { // authentification employé

			if (Controle.authentificationEmploye(in, choix, cheminFichierMdp)) { // authentification via mot de passe
				affichageMenu(); // authentification réussit
				while (choixEmploye(in)) { // affichage menu employé jusqu' a ce qu'il décide de quitter
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
	 * Affichage Menu employé
	 */
	public void affichageMenu() {
		System.out.println("---------------------   MENU HOTEL CDA JAVA  ------------------");
		System.out.println("");
		System.out.println("A- Afficher l'état de l'hôtel");
		System.out.println("B- Afficher le nombre de chambres réservées");
		System.out.println("C- Afficher le numéro de chambres libres");
		System.out.println("D- Afficher le numéro de la première chambre vide");
		System.out.println("E- Afficher le numéro de la dernière chambre vide");
		System.out.println("F- Réserver une chambre");
		System.out.println("G- Libérer une chambre");
		System.out.println("H- Modifier une réservation");
		System.out.println("I- Annuler une réservation");
		System.out.println("Q- Quitter");
		System.out.println("");
		System.out.println("---------------------------------------------------------------");
		System.out.println("Votre choix :");
	}

	/**
	 * Traitement du choix employé
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 * @return : false si l'employé veut quitter le Menu, true sinon
	 */
	public boolean choixEmploye(Scanner in) {
		String choix = in.nextLine();

		switch (choix) {
		case "A":afficherEtatHotel();
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
			employe.reservationChambre(this, in);
			break;
		case "G":
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
	 * methode de création de l'employé de l'hotel
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 */
	private void creationEmploye(Scanner in) {
		String login;
		String motDePasse;
		String nom;
		String prenom;

		// Demande des informations pour la création de l'employé
		System.out.println("Entrer le login de l'employé : ");
		login = Saisie.saisieLoginEmploye(in);
		System.out.println("Entrer le mot de passe de l'employé : ");
		motDePasse = Saisie.saisieNonVide(in);
		System.out.println("Entrer le nom de l'employé : ");
		nom = Saisie.saisieAlphabetic(in);
		System.out.println("Entrer le prenom de l'employé : ");
		prenom = Saisie.saisieAlphabetic(in);

		// instantiation de l'employé de la banque
		employe = new Employe(login, nom, prenom);

		// ajout du login et mot de passe de l'employé au fichier mot de passe
		Fichier.ecritureFichier(cheminFichierMdp, "employe;" + login + ";" + motDePasse, false);

	}

	/**
	 * retourne la liste des logins Client ayant créer un profil dans l'hotel
	 * 
	 * @return un tableau de chaine de caractères représentant la liste des login
	 *         déjà existant
	 */
	public String[] listeLoginClient() {
		if (listeClients == null) { // si aucun client dans l'hotel retour d'un tableau avec une chaine vide
			String[] listeLogin = { "" };
			return listeLogin;
		}
		String[] listeLogin = new String[listeClients.length]; // creation d'un tableau contenant la liste des logins
		for (int i = 0; i < listeLogin.length; i++) {
			listeLogin[i] = listeClients[i].getLogin();
		}
		return listeLogin;
	}

	/**
	 * ajoute un nouveau client dans la liste client
	 * 
	 * @param client : le Client à ajouter
	 */
	public void ajoutClient(Client client) {

		if (listeClients == null) { // si aucun Client dans l'hotel
			listeClients = new Client[1];
			listeClients[0] = client;
			return;
		}

		Client[] listeClientTemp = new Client[listeClients.length]; // recupération de la liste Client de l'hotel
		for (int i = 0; i < listeClientTemp.length; i++) {
			listeClientTemp[i] = listeClients[i];
		}

		listeClients = new Client[listeClientTemp.length + 1]; // création d'une nouvelle liste Client de taille + 1 par
																// rapport à la précédante

		for (int i = 0; i < listeClientTemp.length; i++) { // copie de l'ancienne liste dans la nouvelle
			listeClients[i] = listeClientTemp[i];
		}
		listeClients[listeClients.length - 1] = client; // ajout du nouveau Client en dernière position

	}

	/**
	 * Affiche la liste de clients de l'hotel
	 */
	public void afficherListeClient() {
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
				System.out.println("Le numéro de la première chambre libre est : " + listeChambres[i].getNumero());
				return;
			}
		}

	}

	public void afficherNumeroDerniereChambreLibre() {

		for (int i = listeChambres.length - 1; i >= 0; i--) {
			if (!listeChambres[i].isReserve(LocalDate.now())) {
				System.out.println("Le numéro de la dernière chambre libre est : " + listeChambres[i].getNumero());
				return;
			}
		}
	}

	public Chambre[] listeChambreDispo(LocalDate dateDebut, LocalDate dateFin) {
		Chambre[] chambresDisponiblesTemp = new Chambre[listeChambres.length];
		Chambre[] chambresDisponibles=null;
		int indiceChambreDispo = 0;
		boolean identique;

		for (int i = 0; i < listeChambres.length; i++) {

			 if(!listeChambres[i].isReservePeriode(dateDebut, dateFin)) {

			identique = false;
			for (int j = 0; j < indiceChambreDispo; j++) {
				if (listeChambres[i].isIdentique(chambresDisponiblesTemp[j])) {
					identique = true;
				}
			}
			if (!identique) {

				chambresDisponiblesTemp[indiceChambreDispo] = listeChambres[i];
				indiceChambreDispo++;

			}

		}
		 }
		if (indiceChambreDispo > 0) {
			chambresDisponibles = new Chambre[indiceChambreDispo - 1];
			for (int i = 0; i < chambresDisponibles.length; i++) {
				chambresDisponibles[i] = chambresDisponiblesTemp[i];
			}
		}

		return chambresDisponibles;
	}
	
	
	/**
	 * Methode qui permet d'afficher l' état de l'hotel 
	 * 
	 * @param liste chambres : represente toutes les chambres de l'hotel
	 * @param nom : le nom du client
	 * @param superficie :  représente la superficie
 	 * @param vue : répresente la(les) vue(s) de la chambre
 	 * @param chambre occupée : savoir si une chambre est occupée à la date 
 	 * @param liste reservations : liste des réservations
	 */
	public void afficherEtatHotel() {
		for (int i =0; i<listeChambres.length; i++)
				listeChambres[i].afficherEtatChambre();
			}

}
