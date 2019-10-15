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
		String loginClient="";
		int indiceClient=0;
		LocalDate dateDebut;
		LocalDate dateFin;
		int indiceChambreReserve=0;
		Chambre[] chambresDisponible;
		int i;
		
		//authentification employé si echoue retour menu employé 
		if(!Controle.authentificationEmploye(in, login, hotel.getCheminFichierMdp())) {
			System.out.println("Erreur authentification");
			return;
		}

		if (hotel.getListeClients()==null) { // l'hotel n'a pas de clients
			loginClient = creationNouveauClient(in,hotel);
		} else {
			System.out.println("Etes vous un nouveau client ? (oui ou non)");
			if (Saisie.saisieOuiouNon(in).equals("oui")) { // réservation d'un nouveau client
				loginClient = creationNouveauClient(in,hotel);
			} else { // reservation d'un client fidèle à l'hotel
				System.out.println("Voici la liste des clients de l'hotel : ");
				hotel.afficherListeClient();
				System.out.println("Veuillez entrer le login du client");
				loginClient = Saisie.saisieLoginExistant(in, hotel.listeLoginClient());
			}
		}
		
		//récupération de l'indice du client de la réservation dans la liste clients de l'hotel
		for (  i = 0; i < hotel.getListeClients().length; i++) { 
			if(hotel.getListeClients()[i].getLogin().equals(loginClient)) {
				indiceClient=i;
				break;
			}
		}
		
		//verification du nombre de réservation du client
		if(hotel.getListeClients()[indiceClient].getNombreDeReservations() >=5) {
			System.out.println("Désolé, vous avez déjà 5 réservations dans notre hotel");
			return;
		}
		
		//demande des dates de séjour valide
		System.out.println("quel est votre date de début de séjour dans notre hotel (format dd/MM/yyyy)");
		dateDebut= LocalDate.parse(Saisie.saisieDate(in, LocalDate.now()), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		System.out.println("quel est votre date de fin du séjour dans notre hotel");
		dateFin= LocalDate.parse(Saisie.saisieDate(in, dateDebut.plusDays(1)), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		//recherche des chambres disponibles date (capacité?)
		chambresDisponible=hotel.listeChambreDispo(dateDebut, dateFin);
		for ( i = 0; i < chambresDisponible.length; i++) {
			System.out.println(i + " : " + chambresDisponible[i]);
		}
		System.out.println(i + " : aucune chambres proposées vous convient, annulation de la réservation");
		//affichage chambres
		//choix utilisateur
		System.out.println("quelle chambre voulez-vous reserver ? (choix entre 0 et " + i);
		indiceChambreReserve=Saisie.saisieChoixInt(in, i);
		if(indiceChambreReserve==i) {
			System.out.println("Désolé que aucune chambre vous convienne");
			return;
		}
		 
		System.out.println("Voici la chambre que vous souhaitez : " + chambresDisponible[indiceChambreReserve]);
		System.out.println("Voulez-vous reservé cette chambre ?");
		reponse=Saisie.saisieOuiouNon(in);
		if(reponse.equals("non")) {
			System.out.println("Désolé, que notre hotel ne vous convienne pas");
			return;
		}
		
		//creation de la reservation
		Reservation reservation = new Reservation(dateDebut, dateFin,  hotel.getListeChambres()[indiceChambreReserve], hotel.getListeClients()[indiceClient]);

		
		//ajout de la réservation à la liste Reservation de la chambre
		hotel.getListeChambres()[indiceChambreReserve].ajoutReservation(reservation);
		
		//+1 au nombre de réservations du client
		hotel.getListeClients()[indiceClient].setNombreDeReservations(hotel.getListeClients()[indiceClient].getNombreDeReservations()+1);

		
		reservation.affichage();
		reservation.payement(reservation.getMontantTotal(), in, hotel.getCheminDossierTransaction());
		//envoie facture
		//alimentation fichier transactions
		
		
	}

	/**
	 * Création d'un nouveau Client au sein de l'hotel
	 * @param in : Le Scanner pour la saisie utilisateur
	 * @param hotel : L'hotel dans lequel est créé le nouvaeu client
	 * @return une chaine de caractères représentant le login du nouveau client
	 */
	public String creationNouveauClient(Scanner in,  Hotel hotel) {
		String login;
		String nom;
		String prenom;
		String mail;
		
		//saisie des informations concernant le client
		System.out.println("Entrer le login du client (10 chiffres)");
		login=Saisie.saisieLoginClient(in);
		while(!Controle.isUnique(login, hotel.listeLoginClient())) { //controle si le login n'est pas déjà existant
			System.out.println("Ce login existe déjà dans notre hotel, veuillez choisir un notre login");
			login=Saisie.saisieLoginClient(in);
		}
		
		System.out.println("Entrer le nom client (uniquement des lettres)");
		nom=Saisie.saisieAlphabetic(in);
		
		System.out.println("Entrer le prenom client (uniquement des lettres)");
		prenom=Saisie.saisieAlphabetic(in);
		
		System.out.println("Entrer le mail client (adresse valide)");
		mail=Saisie.saisieMail(in);
		
		Client client=new Client(login, nom, prenom, mail);
		hotel.ajoutClient(client);
		
		return login;
		

	}

	
}
