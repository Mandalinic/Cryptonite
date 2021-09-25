package fr.isika.al9.userRegistration.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
	
	private final JavaMailSender mailSender;
	private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Override
	@Async
	public void send(String to, String email) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setText(email, true);
			helper.setTo(to);
			helper.setSubject("Confirmer votre email");
			helper.setFrom("validation@cryptonite.com");
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			LOGGER.error("Erreur lors de l'envoi de l'email ", e);
			throw new IllegalStateException("Erreur lors de l'envoi de l'email ");
		}
		
	}

}
