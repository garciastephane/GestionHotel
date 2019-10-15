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


	public void setLogin(String login_) {
		login = login_;
	}

	public String getLogin() {
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

	public String getPrenom() {
		return prenom;
	}

	public void setMail(String mail_) {
		mail = mail_;
	}

	public String getMail() {
		return mail;
	}

	public void setNombreDeReservations(int nombreDeReservations_) {
		nombreDeReservations = nombreDeReservations_;
	}

	public int getNombreDeReservations() {
		return nombreDeReservations;
	}


	public void afficherReservations() {
		// TODO Auto-generated method stub
		
	}

	

}
