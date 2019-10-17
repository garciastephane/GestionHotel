package fr.afpa.objets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Employe {
	private String login;
	private String nom;
	private String prenom;

	/**
	 * @param login
	 * @param nom
	 * @param prenom
	 */
	public Employe(String login_, String nom_, String prenom_) {
		login = login_;
		nom = nom_;
		prenom = prenom_;
	}

	public void setLogin(String login_) {
		login = login_;
	}

	public String getlogin() {
		return login;
	}

	public void setNom(String nom_) {
		nom = nom_;
	}

	public String getNom() {
		return nom;
	}

	public void setPrenom(String prenom_) {
		prenom = prenom_;
	}

	public String getprenom() {
		return prenom;
	}

	public void reservationChambre(Hotel hotel, Scanner in) {
		String reponse = "";
		String loginClient = "";
		int indiceClient = 0;
		LocalDate dateDebut;
		LocalDate dateFin;
		int indiceChambreReserve = 0;
		Chambre[] chambresDisponible;
		Client clientReservation=null;
		int i;

		// authentification employ� si echoue retour menu employ�
		if (!Controle.authentificationEmploye(in, login, hotel.getCheminFichierMdp())) {
			System.out.println("Erreur authentification");
			return;
		}

		if (hotel.listeLoginClient()[0].equals("")) { // l'hotel n'a pas de clients
			System.out.println("il n'y a pas encore de client dans l'hotel");
			clientReservation = creationNouveauClient(in, hotel);
		} else {
			System.out.println("Etes vous un nouveau client ? (oui ou non)");
			if (Saisie.saisieOuiouNon(in).equals("oui")) { // r�servation d'un nouveau client
				clientReservation = creationNouveauClient(in, hotel);
			} else { // reservation d'un client fid�le � l'hotel
				System.out.println("Voici la liste des clients de l'hotel : ");
				hotel.afficherListeClient();
				System.out.println("Veuillez entrer le login du client");
				loginClient = Saisie.saisieLoginExistant(in, hotel.listeLoginClient());
				clientReservation=hotel.clientReservation(loginClient);
			}
		}

		// r�cup�ration de l'indice du client de la r�servation dans la liste clients de
		// l'hotel
		if(clientReservation==null) {
			System.out.println("ERREUR");
			return;
		}

		// verification du nombre de r�servation du client
		if (clientReservation.getNombreDeReservations() >= 5) {
			System.out.println("D�sol�, vous avez d�j� 5 r�servations dans notre hotel");
			return;
		}

		// demande des dates de s�jour valide
		System.out.println("quel est votre date de d�but de s�jour dans notre hotel (format dd/MM/yyyy)");
		dateDebut = LocalDate.parse(Saisie.saisieDate(in, LocalDate.now()), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		System.out.println("quel est votre date de fin du s�jour dans notre hotel");
		dateFin = LocalDate.parse(Saisie.saisieDate(in, dateDebut.plusDays(1)),
				DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		// recherche des chambres disponibles aux dates demand�es et si il n'y a pas
		// d�j� 5 reservations dans la chambres (capacit�?)
		chambresDisponible = hotel.listeChambreDispo(dateDebut, dateFin);
		if (chambresDisponible == null) {
			System.out.println("D�sol� nous n'avons aucune chambre � vous proposer");
			return;
		}

		// affichage chambres disponibles
		for (i = 0; i < chambresDisponible.length; i++) {
			System.out.println(i + " : " + chambresDisponible[i]);
		}
		System.out.println(i + " : aucune chambres propos�es vous convient, annulation de la r�servation");

		// choix utilisateur
		System.out.println("quelle chambre voulez-vous reserver ? (choix entre 0 et " + i);
		indiceChambreReserve = Saisie.saisieChoixInt(in, 0, i);
		if (indiceChambreReserve == i) {
			System.out.println("D�sol� que aucune chambre vous convienne");
			return;
		}

		// creation de la reservation
		Reservation reservation = new Reservation(dateDebut, dateFin, clientReservation, chambresDisponible[indiceChambreReserve].getTarif());

		reservation.affichage(chambresDisponible[indiceChambreReserve]);
		System.out.println("Confirmation de la r�servation de la chambre (oui ou non) ?");
		reponse = Saisie.saisieOuiouNon(in);

		if (reponse.equals("non")) {
			System.out.println("D�sol�, que notre hotel ne vous convienne pas");
			return;
		}

		// ajout de la r�servation � la liste Reservation de la chambre
		chambresDisponible[indiceChambreReserve].ajoutReservation(reservation);

		// +1 au nombre de r�servations du client
		clientReservation.setNombreDeReservations(clientReservation.getNombreDeReservations() + 1);

		// payement de la reservation + alimentation fichier transactions
		reservation.payement(reservation.getMontantTotal(), in, hotel.getCheminDossierTransaction());

		// envoie facture
		Facture.creationFacture(reservation, "nouvelle", reservation.getMontantTotal());
		//Facture.creationFacture(reservation, "annulation", -(reservation.getMontantTotal()));
	}

	/**
	 * Cr�ation d'un nouveau Client au sein de l'hotel
	 * 
	 * @param in    : Le Scanner pour la saisie utilisateur
	 * @param hotel : L'hotel dans lequel est cr�� le nouvaeu client
	 * @return une chaine de caract�res repr�sentant le login du nouveau client
	 */
	public Client creationNouveauClient(Scanner in, Hotel hotel) {
		String login;
		String nom;
		String prenom;
		String mail;

		System.out.println("--------    Cr�ation nouveau client  -------------");
		// saisie des informations concernant le client
		System.out.println("Entrer le login du client (10 chiffres)");
		login = Saisie.saisieLoginClient(in);
		while (!Controle.isUnique(login, hotel.listeLoginClient())) { // controle si le login n'est pas d�j� existant
			System.out.println("Ce login existe d�j� dans notre hotel, veuillez choisir un notre login");
			login = Saisie.saisieLoginClient(in);
		}

		System.out.println("Entrer le nom client (uniquement des lettres)");
		nom = Saisie.saisieAlphabetic(in);

		System.out.println("Entrer le prenom client (uniquement des lettres)");
		prenom = Saisie.saisieAlphabetic(in);

		System.out.println("Entrer le mail client (adresse valide)");
		mail = Saisie.saisieMail(in);
		System.out.println("-----------------------------------------------");
		return new Client(login, nom, prenom, mail);
		

	}

	public void liberationChambre(Scanner in, Hotel hotel) {
		int choix;
		int numeroChambre;
		String nomClient;
		int nbClientNom=0;
		String loginClient="";
		Chambre[] chambresClient = new Chambre[5];
		int indiceChambreClient = 0;
		Client clientLiberation;
		
		
		System.out.println("1 : lib�ration en utilisant le  num�ro de chambre. \n 2 : lib�ration via nom client.");
		choix = Saisie.saisieChoixInt(in, 1, 2);
		if (choix == 1) {
			System.out.println("Veuillez entrer le numero de chambre : ");
			numeroChambre = Saisie.saisieChoixInt(in, 1, hotel.getListeChambres().length);
			if (hotel.getListeChambres()[numeroChambre - 1].isReserve(LocalDate.now())) {
				hotel.getListeChambres()[numeroChambre - 1].liberationChambre();
				return;
			} else {
				System.out.println("Cette chambre n'est pas occup�e en ce moment");
				return;
			}
		} else {
			
			System.out.println("Veuillez entrer le nom du client : ");
			nomClient = Saisie.saisieAlphabetic(in);
			
			// veriif si client existe recup login gestion 2 m�me nom
			for (int i = 0; i < hotel.getListeClients().length; i++) {
				if(hotel.getListeClients()[i].getNom().equals(nomClient)) {
					nbClientNom++;
					clientLiberation=hotel.getListeClients()[i];
				}
			}
			if(nbClientNom==0) { //aucun client avec ce nom existe
				System.out.println("Il n'y a aucun client avec ce nom dans notre hotel");
				return;
			}
			if(nbClientNom>1) { // plus de un client � le m�me nom
				
				Client[] clients = new Client[nbClientNom]; //liste de clients ayant le meme nom
				nbClientNom=0;
				for (int i = 0; i < hotel.getListeClients().length; i++) {
					if(hotel.getListeClients()[i].getNom().equals(nomClient)) {
						clients[nbClientNom]=hotel.getListeClients()[i];
						nbClientNom++;
						clientLiberation=hotel.getListeClients()[i];
					}
				}
				System.out.println("Il y a plusieurs clients avec le nom demand�. Veuillez choisir le client correspondant");
				for (int i = 0; i < clients.length; i++) {
					System.out.println( i + " : " + clients[i].getLogin() + "nom : " + clients[i].getNom() + "prenom : " + clients[i].getPrenom());
				}
				System.out.println("Votre choix  : ");
				choix=Saisie.saisieChoixInt(in, 0, clients.length-1);
				clientLiberation=clients[choix];	
			}
			
		
			
			// parcours de la liste des chambres de  l'hotel
			for (int i = 0; i < hotel.getListeChambres().length; i++) {

				// si la chambre est occup� en ce moment
				if (hotel.getListeChambres()[i].isReserve(LocalDate.now())) { 

					// parcours des  reservations  de la chambre
					for (int j = 0; j < hotel.getListeChambres()[i].getListeReservations().length; j++) { 

						// si la reservation est en cours et elle apartient au client ajout a la liste
						// des chambres du client suseptible d'�tre lib�r�
						if (hotel.getListeChambres()[i].getListeReservations()[j]!=null&& hotel.getListeChambres()[i].getListeReservations()[j].isEnCours()
								&& hotel.getListeChambres()[i].getListeReservations()[j].getClient().getLogin()
										.equals(loginClient)) {
							chambresClient[indiceChambreClient] = hotel.getListeChambres()[i];
							indiceChambreClient++;
						}
					}
				}

			}
			
			//aucune chambre n'est occup�e en ce moment par le client
			if(indiceChambreClient==0) {
				System.out.println("Ce client n'occupe pas de chambre en ce moment");
				return;
			}
			
			//une seule chambre est occup�e par le client, lib�ration de cette chambre
			if(indiceChambreClient==1) {
				chambresClient[0].liberationChambre();
			}
			
			//choix de la chambre � lib�rer
			System.out.println("Voici la liste des chambres que  le client occupe en ce moment  : ");
			for (int i = 0; i < indiceChambreClient; i++) {
				System.out.println(i + " : " + chambresClient[i].getNumero());
			}
			System.out.println("Entrer votre choix :  ");
			chambresClient[Saisie.saisieChoixInt(in, 0, indiceChambreClient-1)].liberationChambre();
			
		}

	}

}
