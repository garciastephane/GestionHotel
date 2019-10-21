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
	/** modifie le login de l employe
	*
	* @param login_ : le login de l employe
	*/
	public void setLogin(String login_) {
		login = login_;
	}
	/**
	 * retourne le nouveau login de l employe
	 *
	 * @return : une chaine de caractere correspondant au login de l employe
	 */
	public String getlogin() {
		return login;
	}
	/** modifie le nom de l employe
	*
	* @param nom_ : le nom de l employe
	*/
	public void setNom(String nom_) {
		nom = nom_;
	}
	/**
	 * retourne le nouveau nom de l employe
	 *
	 * @return : une chaine de caractere correspondant au nom de l employe
	 */
	public String getNom() {
		return nom;
	}
	/** modifie le prenom de l employe
	*
	* @param prenom_ : le prenom de l employe
	*/
	public void setPrenom(String prenom_) {
		prenom = prenom_;
	}
	/**
	 * retourne le nouveau prenom de l employe
	 *
	 * @return : une chaine de caractere correspondant au prenom de l employe
	 */
	public String getprenom() {
		return prenom;
	}

	
	/**
	 * Création d'un nouveau Client au sein de l'hotel
	 * 
	 * @param in    : Le Scanner pour la saisie utilisateur
	 * @param hotel : L'hotel dans lequel est créé le nouvaeu client
	 * @return une chaine de caractères représentant le login du nouveau client
	 */
	public Client creationNouveauClient(Scanner in,String[] listeLoginExistant) {
		String login;
		String nom;
		String prenom;
		String mail;

		System.out.println("--------    Création nouveau client  -------------");
		// saisie des informations concernant le client
		System.out.println("Entrer le login du client (10 chiffres)");
		login = Saisie.saisieLoginClient(in);
		while (!Controle.isUnique(login, listeLoginExistant)) { // controle si le login n'est pas déjà existant
			System.out.println("Ce login existe déjà dans notre hotel, veuillez choisir un notre login");
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


	

}
