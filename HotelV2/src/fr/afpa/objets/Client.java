package fr.afpa.objets;

public class Client {

	private String login;
	private String nom;
	private String prenom;
	private String mail;
	private int nombreDeReservations;
	
	/**
	 * @param login
	 * @param nom
	 * @param prenom
	 * @param mail
	 */
	public Client(String login_, String nom_, String prenom_, String mail_) {
		login = login_;
		nom = nom_;
		prenom = prenom_;
		mail = mail_;
		nombreDeReservations=0;
	}
	
	
	@Override
	public String toString() {
		return "Client [login=" + login + ", nom=" + nom + ", prenom=" + prenom + "]";
	}

	/** modifie le login du client
	*
	* @param login_ : le login du client
	*/
	public void setLogin(String login_) {
		login = login_;
	}
	/**
	 * retourne le nouveau login du client
	 *
	 * @return : une chaine de caractere correspondant au login du client
	 */
	public String getLogin() {
		return login;

	}
	/** modifie le nom du client
	*
	* @param nom_ : le nom du client
	*/
	public void setNom(String nom_) {
		nom = nom_;
	}
	/**
	 * retourne le nouveau nom du client de la chambre
	 *
	 * @return : une chaine de caractere correspondant au nom du client
	 */
	public String getNom() {
		return nom;
	}
	/** modifie le prenom du client de la chambre
	*
	* @param prenom_ : le prenom du client
	*/
	public void setPrenom(String prenom_) {
		prenom = prenom_;
	}
	/**
	 * retourne le nouveau prenom du client de la chambre
	 *
	 * @return : une chaine de caractere correspondant au prenom du client
	 */
	public String getPrenom() {
		return prenom;
	}
	/** modifie le mail du client
	*
	* @param mail_ : le mail du client
	*/
	public void setMail(String mail_) {
		mail = mail_;
	}
	/**
	 * retourne le nouveau mail du client
	 *
	 * @return : une chaine de caractere correspondant au mail du client
	 */
	public String getMail() {
		return mail;
	}
	/** modifie le nombre de reservations du client
	*
	* @param nombresDeReservations : le nombre de reservations du client
	*/
	public void setNombreDeReservations(int nombreDeReservations_) {
		nombreDeReservations = nombreDeReservations_;
	}
	/**
	 * retourne le nouveau numero de reservations du client
	 *
	 * @return : un entier correspondant au numero de reservations du client
	 */
	public int getNombreDeReservations() {
		return nombreDeReservations;
	}

	

}
