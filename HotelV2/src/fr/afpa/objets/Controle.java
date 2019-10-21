package fr.afpa.objets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Controle {

	/**
	 * methode qui retourne true si la chaine de caractères est de type numérique et
	 * de la bonne taille
	 * 
	 * @param chaine : chaine de caractères à tester
	 * @param taille : taille que la chaine de caractères doit avoir
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
	 * methode qui retourne true si la chaine de caractères est de type
	 * alpha-numérique(commence par 2 majuscules (GH puis des chiffres) et de la bonne
	 * taille
	 * 
	 * @param chaine : chaine de caractère à tester
	 * @param taille : taille que la chaine de caractères doit avoir
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
	 * methode qui verifie si la réponse est oui ou non
	 * 
	 * @param reponse : chaine de caractères à vérifier
	 * @return true si la reponse est 'o' ou 'n', false sinon
	 */
	public static boolean isOuiOuNon(String reponse) {
		if (!(reponse.equals("oui") || reponse.equals("non"))) {
			return false;
		}
		return true;
	}

	/**
	 * methode qui verifie si la chaine de caracteres est non vide (les espaces sont
	 * considéré comme vide)
	 * 
	 * @param chaine chaine de caractères à vérifier
	 * @return true si la chaine de caractères est non vide et n'est pas constituée
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

	/**
	 * methode qui confirme si le mail entré est au format valide
	 * 
	 * @param mail avec les paramètres qui valident un mail
	 * @return true si la chaine est un mail valide, false sinon
	 */

	public static boolean isMail(String mail) {
		//  retourne false si le mail est vide
		if (mail == null)
			return false;
		// retourne false si la longueur du mail est inferieur a 5 caracteres
		if (mail.length()<5)
			return false;
						
		// un string avec . apparaît deux fois de suite
		if (mail.contains("..") )
			return false;
		// un string avec un point avant arobase
		if (mail.contains(".@") )
			return false;
		// un string avec un @ entre deux mots
		String [] parts = mail.split("@");
		
		if(parts.length != 2) {
		
			return false; }
		// un string une premiere partie qui n est pas vide
			String part1 =parts[0];
			if(!isNonVide(part1)) {
				
				return false;}
			// un string avec une deuxieme partie qui n est pas vide		
			String part2 = parts[1];
			// un string avec une trosieme partie qui n est pas vide
			String [] part3 =part2.split("\\.");
			
			if(part3.length!=2) {
				
				return false;}

			if(!isAlphabetic(part3[0])) {
			
				return false;
			}
			
			if(!isAlphabetic(part3[1])) {
			
				return false;
			}
			 return true;
					
			
	}
			
	

	/**
	 * methode qui retourne si la chaine de caractères est une date valide
	 * 
	 * @param date chaine de caractère à tester (jj/mm/aaaa)
	 * @return true si la chaine est une date valide, false sinon
	 */
	public static boolean isDateValide(String date) {

		try {
            LocalDate date2=LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }catch(Exception e) {
            return false;
        }
        return true;

	}

	/**
	 * methode qui vérifie si la chaine est composee uniquement de caractères
	 * alphabetique
	 * 
	 * @param reponse
	 * @return false si chaine n est pas composee que de caracteres alphabetiques sinon true
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
	/**
	 * methode qui vérifie authantification de l'employe
	 * @param in Scanner pour recuperer le mot de passe de l'employe
	 * @param choix correspond au choix de l'employe
	 * @param cheminFichierMdp fichier ou sont stockes les mots de passe des employes
	 * @return true si authentification valide false si authentification incorrecte
	 */
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
