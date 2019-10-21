package fr.afpa.objets;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail {

	public static void envoiMail (String cheminPieceJointe, String adresseMail)  {

		try {	
				//String adresse = "garciastephan60@gmail.com";

		
			/**
			 * ouverture d'une session
			 * @param user : mail envoi 
			 * @param pass: mot de passe mail envoi
			 * @param serveur : serveur envoi du mail  
			 */
						
		String user = "roubaixmailtestafpa@gmail.com";
		String pass = "afpa2019";
		String serveur = "smtp.gmail.com";
		
		Properties props= System.getProperties();
		props.put("mail.smtp.host", serveur);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", 587);
		props.put("mail.smtp.auth", "true");
		Session session= Session.getInstance(props);
				
	
		/**
		 * creation du message
		 * @param session : mail envoi 
		 * @param user: mot de passe mail envoi
		 */
			
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(user));
		
		InternetAddress[] internetAdresses = new InternetAddress[1];
		internetAdresses[0] = new InternetAddress(adresseMail);
		message.setRecipients(Message.RecipientType.TO, internetAdresses);
		message.setSubject("Mail de confirmation de réservation");
	    message.setText("");
	    
	    /**
		 * ajout piece jointe
		 * @param cheminPieceJointe: le chemin ou est stocke la piece jointe
		 */
	 // La pièce jointe
	 	MimeBodyPart attachment = new MimeBodyPart();
	 	attachment.attachFile(cheminPieceJointe);
	 	
	 	/**
		 * ajout du corps du message
		 
		 */	
	 	// Le corps
	 	MimeBodyPart body = new MimeBodyPart();
		body.setText("Madame / Monsieur,\n Nous vous confirmons la réservation de votre chambre dans notre hôtel CDA. Ci-joint la facture.\n En vous remerciant par avance, nous vous adressons nos salutations distinguées.\n\n ");
		//MimeBodyPart bodyText = new MimeBodyPart();
		//String fichier = "C:\\Bureau\\mai.text";
		//bodyText.attachFile(fichier);
	 		
	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(body);
	    multipart.addBodyPart(attachment);
	    //multipart.addBodyPart(bodyText);
	    message.setContent(multipart);
	    /**
		 * envoi du message
		 * @param serveur : connection au serveur pour l'envoi 
		 * @param user: user d ou part l envoi du mail
		 * @param message: message a envoyer
		 * @param internetAdresses:adresse qui recevra l envoi
		 */ 
		//Transport
	    Transport transport = session.getTransport("smtp");
		transport.connect(serveur, user, pass);
		transport.sendMessage(message, internetAdresses);
		transport.close();
	    
	   
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
}
	}

