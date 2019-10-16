package fr.afpa.objets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Saisie {




	/**
	 * demande une reponse oui ou non jusqu'a ce quelle soit valide
	 * 
	 * @param in le Scanner pour la saisie utilisateur
	 * @return une reponse oui ou non
	 */
	public static String saisieOuiouNon(Scanner in) {
		String reponse = "";
		reponse = in.nextLine();
		while (!Controle.isOuiOuNon(reponse)) {
			System.out.println("Veuillez entrer oui ou non");
			reponse = in.nextLine();
		}
		return reponse;
	}

	/**
	 * demande des mots jusqu'a ce la reponse soit valide
	 * 
	 * @param in : le Scanner pour la saisie utilisateur
	 * @return des mots
	 */
	public static String saisieNonVide(Scanner in) {
		String reponse = "";
		reponse = in.nextLine();
		while (!Controle.isNonVide(reponse)) {
			System.out.println("Veuillez entrer quelques chose");
			reponse = in.nextLine();
		}
		return reponse;
	}

	/**
	 * demande une adresse mail valide
	 * @param in : le Scanner pour la saisie utilisateur
	 * @return une addresse mail valide
	 */
	public static String saisieMail(Scanner in) {

		String mail = "";
		mail = in.nextLine();
		while (!Controle.isMail(mail)) {
			System.out.println("Veuillez entrer un mail valide");
			mail = in.nextLine();

		}
		return mail;
	}

	/**
	 * demande la saisie d'un login jusqu'a ce que le login existe au sein de la
	 * liste passée en paramètre
	 * 
	 * @param in            : le Scanner pour la saisie
	 * @param loginExistant : tableau de String représentant les login existant
	 * @return : une chaine de caractères représentant un login existant
	 */
	public static String saisieLoginExistant(Scanner in, String[] loginExistant) {
		String login;
		login = in.nextLine();
		while (Controle.isUnique(login, loginExistant)) {
			System.out.println("Veuillez entrer un login existant");
			login = in.nextLine();

		}
		return login;
	}

	/**
	 * demande une date valide après la date passée en paramètre
	 * @param in  : le Scanner pour la saisie
	 * @param date : date minimum
	 * @return une date valide après ou égale la date passée en paramètre
	 */
	public static CharSequence saisieDate(Scanner in, LocalDate date) {

		String dateSaisie;
		dateSaisie = in.nextLine();

		while (!Controle.isDateValide(dateSaisie) || date.isAfter(LocalDate
				.parse(dateSaisie.subSequence(0, dateSaisie.length()), DateTimeFormatter.ofPattern("dd/MM/yyyy")))) {
			System.out.println("Veuillez entrez une date valide  après le " + date.minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +" (format dd/MM/yyyy)");
			dateSaisie = in.nextLine();
		}
		return dateSaisie.subSequence(0, dateSaisie.length());

	}

	/**
	 * 
	 * @param in
	 * @param max
	 * @return
	 */
	public static int saisieChoixInt(Scanner in,int min, int max) {
		String reponse = in.nextLine();

		while (!Controle.isNumerique(reponse, reponse.length()) || Integer.parseInt(reponse) > max || Integer.parseInt(reponse)<min) {
			System.out.println("Veuillez entrez un nombre valide correspondant au choix ci-dessus : entre "+ min + " et " + max);
			reponse = in.nextLine();
		}
		return Integer.parseInt(reponse);

	}

	/**
	 * demande un login client valide
	 * @param in : le Scanner pour la saisie
	 * @return : un login client valide
	 */
	public static String saisieLoginClient(Scanner in) {
		String reponse = in.nextLine();

		while (!Controle.isNumerique(reponse, 10)) {
			System.out.println("Veuillez entrez un login valide (10 chiffres)");
			reponse = in.nextLine();
		}
		return reponse;

	}

	/**
	 * demande une reponse composée uniquement de lettres et non vide
	 * @param in : le Scanner pour la saisie
	 * @return : une reponse composée uniquement de lettres et non vide
	 */
	public static String saisieAlphabetic(Scanner in) {
		String reponse = "";
		reponse = in.nextLine();
		while (!Controle.isAlphabetic(reponse)) {
			System.out.println("Veuillez entrer uniquement des lettres");
			reponse = in.nextLine();
		}
		return reponse;
	}

	/**
	 * damande un login employé valide
	 * @param in : le Scanner pour la saisie
	 * @return : un login employé valide
	 */
	public static String saisieLoginEmploye(Scanner in) {
		String reponse = "";
		reponse = in.nextLine();
		while (!Controle.isAlphaNumerique(reponse, 5)) {
			System.out.println("Veuillez entrer un login valide (GH + 3 chiffres)");
			reponse = in.nextLine();
		}
		return reponse;
	}

	
	
}
