package fr.afpa.objets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Fichier {

	/**
	 * methode qui lit un fichier et qui retourne un tableau de chaine de caractères representant les lignes du fichier
	 * @param cheminFichier : chaine de caractères représentant le chemin absolue du fichier
	 * @return un tableau de chaine de caractères représentant les lignes du fichier
	 */
	public static String[] lecture(String cheminFichier) {
		String[] lignes;
		int nbLignes=0;
		int i=0;
		try {
			FileReader fr = new FileReader(cheminFichier);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				br.readLine();
				nbLignes++;
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Erreur " + e);
		}
		lignes=new String[nbLignes];
		try {
			FileReader fr = new FileReader(cheminFichier);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				lignes[i]=br.readLine();
				i++;
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Erreur " + e);
		}
		return lignes;
		
	}

	/**
	 * methode qui écrire dans un fichier
	 * @param chemin : chemin du fichier dans lequels on veut écrire
	 * @param ecritureFichier : lignes que l'on veut ajouter
	 * @param ajouter : true si on veut ajouter, false si on veut écraser le fichier
	 */
	public static void ecritureFichier(String chemin, String ecritureFichier, boolean ajouter) {
		
		try {
			FileWriter fw = new FileWriter(chemin,ajouter);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(ecritureFichier);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			System.out.println("Erreur " + e);
		}

	}
}
