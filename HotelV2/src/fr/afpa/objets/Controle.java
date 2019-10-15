package fr.afpa.objets;

import java.util.Scanner;

public class Controle {

	/**
	 * methode qui retourne true si la chaine de caractère est de type numérique et
	 * de la bonne taille
	 * 
	 * @param chaine : chaine de caractère à tester
	 * @param taille : taille que la chaine de caractère doit avoir
	 * @return true si la chaine est numérique et de la bonne taille, false sinon
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
	 * methode qui retourne true si la chaine de caractère est de type
	 * alpha-numérique(commence par 2 majuscules puis des chiffres) et de la bonne
	 * taille
	 * 
	 * @param chaine : chaine de caractère à tester
	 * @param taille : taille que la chaine de caractère doit avoir
	 * @return true si la chaine est alpha-numérique et de la bonne taille, false
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
	 * methode qui verifie si la chaine de caractère est un double
	 * 
	 * @param decimal : chaine de caractè à tester
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
	 * methode qui verifie si la réponse est oui ou non
	 * 
	 * @param reponse : chaine de caractère à vérifier
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
	 * concidéré comme vide)
	 * 
	 * @param chaine chaine de caractère à vérifier
	 * @return true si la chaine de caractère est non vide et n'est pas constitué
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

	public static boolean isDateValide(String dateSaisie) {
		return true;
	}

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
		motDePasse=in.nextLine();
		for (int i = 0; i < lignesFichier.length; i++) {
			ligne=lignesFichier[i].split(";");
			if(ligne[0].equals("employe") && ligne[3].length()>0 ) {
				if (ligne[1].equals(choix) && ligne[3].equals(motDePasse)) {
					return true;
				}
			}
		}
		return false;

	}
}
