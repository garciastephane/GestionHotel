package fr.afpa.objets;

import java.util.Scanner;

public class Controle {

	/**
	 * methode qui retourne true si la chaine de caract�re est de type num�rique et
	 * de la bonne taille
	 * 
	 * @param chaine : chaine de caract�re � tester
	 * @param taille : taille que la chaine de caract�re doit avoir
	 * @return true si la chaine est num�rique et de la bonne taille, false sinon
	 */
	public static boolean isNumerique(String chaine, int taille) {
		if (chaine.length() == taille) {
			for (int i = 0; i < chaine.length(); i++) {
				if (!Character.isDigit(chaine.charAt(i))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * methode qui retourne true si la chaine de caract�re est de type
	 * alpha-num�rique(commence par 2 majuscules puis des chiffres) et de la bonne
	 * taille
	 * 
	 * @param chaine : chaine de caract�re � tester
	 * @param taille : taille que la chaine de caract�re doit avoir
	 * @return true si la chaine est alpha-num�rique et de la bonne taille, false
	 *         sinon
	 */
	public static boolean isAlphaNumerique(String chaine, int taille) {
		if (chaine.length() == taille && chaine.charAt(0) == 'G' && chaine.charAt(1) == 'H') {
			chaine = chaine.substring(2);
			if (!isNumerique(chaine, chaine.length())) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * methode qui verifie si la chaine de caract�re est un double
	 * 
	 * @param decimal : chaine de caract� � tester
	 * @return true si decimal est du format double
	 */
	public static boolean isDouble(String decimal) {
		String temp;
		if (decimal.length() > 0 && decimal.charAt(0) == '-') {
			decimal = decimal.substring(1);
		}
		temp = decimal.replaceAll("\\.", "");
		if (temp.length() > 0 && temp.length() + 1 >= decimal.length()) {
			if (!isNumerique(temp, temp.length())) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * methode qui verifie si la r�ponse est oui ou non
	 * 
	 * @param reponse : chaine de caract�re � v�rifier
	 * @return true si la reponse est 'o' ou 'n', false sinon
	 */
	public static boolean isOuiOuNon(String reponse) {
		if (!(reponse.equals("oui") || reponse.equals("non"))) {
			return false;
		}
		return true;
	}

	/**
	 * methode qui verifie si la chaine de caractere et non vide (les espaces sont
	 * concid�r� comme vide)
	 * 
	 * @param chaine chaine de caract�re � v�rifier
	 * @return true si la chaine de caract�re est non vide et n'est pas constitu�
	 *         que des espaces, false sinon
	 */
	public static boolean isNonVide(String chaine) {
		if (chaine.replaceAll(" ", "").length() == 0) {
			return false;
		}
		return true;
	}

	public static boolean isUnique(String chaine, String[] tableauExistant) {
		for (int i = 0; i < tableauExistant.length; i++) {
			if (chaine.equals(tableauExistant[i])) {
				return false;
			}
		}
		return true;
	}

	
	
	public static boolean isMail(String mail) {
		return true;

	}

	/**
	 * methode qui retourne si la chaine de caract�re est une date valide
	 * 
	 * @param date chaine de caract�re � tester (jj/mm/aaaa)
	 * @return true si la chaine est une date valide, false sinon
	 */
	public static boolean isDateValide(String date) {

		if (date.length() != 10 || date.charAt(2) != '/' || date.charAt(5) != '/'
				|| !isNumerique(date.replace('/', '0'), date.length())) {
			return false;
		}

		boolean bissextile;
		int jour = Integer.parseInt("" + date.charAt(0) + date.charAt(1));
		int mois = Integer.parseInt("" + date.charAt(3) + date.charAt(4));
		int annee = Integer.parseInt("" + date.charAt(6) + date.charAt(7) + date.charAt(8) + date.charAt(9));

		bissextile = ((annee % 4 == 0 && annee % 100 != 0) || (annee % 4 == 0 && annee % 400 == 0));

		if ((jour < 1 || jour > 31 || mois > 12 || mois < 1 )
				|| ((mois == 4 || mois == 6 || mois == 9 || mois == 11) && jour > 30)
				|| (mois == 2 && jour > 28 && !bissextile) || (mois == 2 && jour > 29 && bissextile)) {

			return false;

		}
		return true;

	}

	/**
	 * methode qui v�rifie si la chaine est compos�e uniquement de caract�res alphabetique
	 * @param reponse
	 * @return
	 */
	public static boolean isAlphabetic(String reponse) {
		if (reponse.replaceAll(" ", "").equals("")) {
			return false;
		}
		for (int i = 0; i < reponse.length(); i++) {
			if (!Character.isAlphabetic(reponse.charAt(i)) && reponse.charAt(i) != ' ' && reponse.charAt(i) != '-') {
				return false;
			}

		}
		return true;
	}

	public static boolean authentificationEmploye(Scanner in, String choix, String cheminFichierMdp) {
		String[] lignesFichier = Fichier.lecture(cheminFichierMdp);
		String[] ligne;
		String motDePasse;
		System.out.print("Mot de passe : ");
		motDePasse = in.nextLine();
		for (int i = 0; i < lignesFichier.length; i++) {
			ligne = lignesFichier[i].split(";");
			if (ligne[0].equals("employe") && ligne[3].length() > 0) {
				if (ligne[1].equals(choix) && ligne[3].equals(motDePasse)) {
					return true;
				}
			}
		}
		return false;

	}
}
