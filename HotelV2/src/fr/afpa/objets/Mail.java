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

		//ouverture d'une session.
						
		String user = "garciastephan60@gmail.com";
		String pass = "Milan2019";
		String serveur = "smtp.gmail.com";
		
		Properties props= System.getProperties();
		props.put("mail.smtp.host", serveur);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", 587);
		props.put("mail.smtp.auth", "true");
		Session session= Session.getInstance(props);
				
		//Le message
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(user));
		
		InternetAddress[] internetAdresses = new InternetAddress[1];
		internetAdresses[0] = new InternetAddress(adresseMail);
		message.setRecipients(Message.RecipientType.TO, internetAdresses);
		message.setSubject("Mail de confirmation de réservation");
	    message.setText("");
	    
	 // La pièce jointe
	 	MimeBodyPart attachment = new MimeBodyPart();
	 	attachment.attachFile(cheminPieceJointe);
	 		
	 	// Le corps
	 	MimeBodyPart body = new MimeBodyPart();
		body.setText("Madame / Monsieur,\n Nous vous confirmons la réservation de votre chambre dans notre hôtel CDA au nom de Madame/Monsieur (nom de la personne) pour la période suivante: (dates).\n En vous remerciant par avance, nous vous adressons, Madame/Monsieur, nos salutations distinguées.\n Signature Electronique\";\r\n ");
		//MimeBodyPart bodyText = new MimeBodyPart();
		//String fichier = "C:\\Bureau\\mai.text";
		//bodyText.attachFile(fichier);
	 		
	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(body);
	    multipart.addBodyPart(attachment);
	    //multipart.addBodyPart(bodyText);
	    message.setContent(multipart);
			
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

