package cl.waypoint.ms.mailer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import cl.waypoint.ms.mailer.dto.Attachment;
import cl.waypoint.ms.mailer.dto.Message;
import cl.waypoint.ms.mailer.timer.TimerBounce;

public class Mailer {
	private static final Logger LOGGER = Logger.getLogger(Mailer.class.getName());
	private static JavaMailSenderImpl mailSender;

	static {
		mailSender = new JavaMailSenderImpl();
		mailSender.setHost("email-smtp.us-west-2.amazonaws.com");
		mailSender.setPort(587);
		mailSender.setUsername("AKIAJKPTU635SQO2KXNA");
		mailSender.setPassword("An21doQY5OnZsvgn+G2aFuACIqRx0RvlnsU64clIDdbT");

		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		mailSender.setJavaMailProperties(props);
	}

	private Mailer() {
		// private constructor to hide the implicit public one.
	}

	public static String send(Message msg) {
		checkParams(msg);
		checkRecipients(msg);
		if (msg.getTo().length == 0 && msg.getCc().length == 0 && msg.getBcc().length == 0) {
			throw new IllegalArgumentException("No valid recipients");
		}
		doSend(msg);
		return msg.toString();
	}

	private static void checkRecipients(Message msg) {
		if (msg.getTo() != null) {
			msg.setTo(filter(msg.getTo()));
		}
		if (msg.getCc() != null) {
			msg.setCc(filter(msg.getCc()));
		}
		if (msg.getBcc() != null) {
			msg.setBcc(filter(msg.getBcc()));
		}

	}

	private static String[] filter(String[] to) {
		List<String> approved = new ArrayList<>();
		for (String addr : to) {
			if (!TimerBounce.isBlacklisted(addr)) {
				// TODO Qué pasa para las direcciones del tipo <me@doomain.com> "Mi nombre"
				approved.add(addr);
			}
		}
		String[] result = new String[approved.size()];
		return approved.toArray(result);
	}

	private static void doSend(Message email) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage msg) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

				if (email.getAttachments() != null) {
					for (Attachment attach : email.getAttachments()) {
						byte[] decodedBytes = Base64.getDecoder().decode(attach.getData());
						InputStream is = new ByteArrayInputStream(decodedBytes);
						helper.addAttachment(attach.getFilename(), new InputStreamResource(is),
								attach.getContentType());
					}
				}

				msg.setRecipients(javax.mail.Message.RecipientType.TO, getAddresses(email.getTo()));
				if (email.getCc() != null) {
					msg.setRecipients(javax.mail.Message.RecipientType.CC, getAddresses(email.getCc()));
				}
				if (email.getBcc() != null) {
					msg.setRecipients(javax.mail.Message.RecipientType.BCC, getAddresses(email.getBcc()));
				}
				msg.setFrom(new InternetAddress(email.getFrom()));
				msg.setSubject(email.getSubject(), "UTF-8");

				if (email.getReplyTo() != null) {
					Address[] replyTo = new Address[1];
					replyTo[0] = new InternetAddress(email.getReplyTo());
					msg.setReplyTo(replyTo);
				}

				msg.setContent(email.getBody(), "text/html; charset=utf-8");
			}

			private Address[] getAddresses(String[] to) throws AddressException {
				Address[] addr = new Address[to.length];
				for (int i = 0; i < to.length; i++) {
					addr[i] = new InternetAddress(to[i]);
				}
				return addr;
			}
		};
		try {
			mailSender.send(preparator);
		} catch (MailException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
		}

	}

	private static void checkParams(Message msg) {
		if (msg == null) {
			throw new IllegalArgumentException("Mensaje vacío");
		}
		if (msg.getFrom() == null || msg.getFrom().isEmpty()) {
			throw new IllegalArgumentException("Emisor vacío");
		}
		if (msg.getTo() == null || msg.getTo().length == 0) {
			throw new IllegalArgumentException("Receptor vacío");
		}
		if (msg.getSubject() == null || msg.getSubject().isEmpty()) {
			throw new IllegalArgumentException("Subject vacío");
		}
		if (msg.getBody() == null || msg.getBody().isEmpty()) {
			throw new IllegalArgumentException("Cuerpo de mensaje vacío");
		}
	}

}
