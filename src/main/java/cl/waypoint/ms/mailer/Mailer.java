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
import cl.waypoint.ms.mailer.rest.RestMessage;
import cl.waypoint.ms.mailer.timer.TimerBounce;

public class Mailer {
	private static final Logger LOGGER = Logger.getLogger(Mailer.class.getName());
	private static JavaMailSenderImpl mailSender;

	static {
		mailSender = new JavaMailSenderImpl();
		mailSender.setHost(Messages.getString("Mailer.host")); //$NON-NLS-1$
		mailSender.setPort(587);
		mailSender.setUsername(Messages.getString("Mailer.username")); //$NON-NLS-1$
		mailSender.setPassword(Messages.getString("Mailer.password")); //$NON-NLS-1$

		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		props.setProperty("mail.smtp.starttls.enable", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		mailSender.setJavaMailProperties(props);
	}

	private Mailer() {
		// private constructor to hide the implicit public one.
	}

	public static RestMessage send(Message msg) {
		checkParams(msg);
		checkRecipients(msg);
		if (msg.getTo().isEmpty() && msg.getCc().isEmpty() && msg.getBcc().isEmpty()) {
			throw new IllegalArgumentException("No valid recipients"); //$NON-NLS-1$
		}
		return doSend(msg);
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

	private static List<String> filter(List<String> to) {
		List<String> approved = new ArrayList<>();
		for (String addr : to) {
			if (!TimerBounce.isBlacklisted(addr)) {
				// TODO Qué pasa para las direcciones del tipo <me@doomain.com> "Mi nombre"
				approved.add(addr);
			}
		}
		return approved;
	}

	private static RestMessage doSend(Message email) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage msg) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8"); //$NON-NLS-1$

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
				msg.setSubject(email.getSubject(), "UTF-8"); //$NON-NLS-1$

				if (email.getReplyTo() != null) {
					Address[] replyTo = new Address[1];
					replyTo[0] = new InternetAddress(email.getReplyTo());
					msg.setReplyTo(replyTo);
				}

				msg.setContent(email.getBody(), "text/html; charset=utf-8"); //$NON-NLS-1$
			}

			private Address[] getAddresses(List<String> to) throws AddressException {
				Address[] addr = new Address[to.size()];
				for (int i = 0; i < to.size(); i++) {
					addr[i] = new InternetAddress(to.get(i));
				}
				return addr;
			}
		};
		try {
			mailSender.send(preparator);
		} catch (MailException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return new RestMessage();

	}

	private static void checkParams(Message msg) {
		if (msg == null) {
			throw new IllegalArgumentException("Mensaje vacío"); //$NON-NLS-1$
		}
		if (msg.getFrom() == null || msg.getFrom().isEmpty()) {
			throw new IllegalArgumentException("Emisor vacío"); //$NON-NLS-1$
		}
		if (msg.getTo() == null || msg.getTo().isEmpty() ) {
			throw new IllegalArgumentException("Receptor vacío"); //$NON-NLS-1$
		}
		if (msg.getSubject() == null || msg.getSubject().isEmpty()) {
			throw new IllegalArgumentException("Subject vacío"); //$NON-NLS-1$
		}
		if (msg.getBody() == null || msg.getBody().isEmpty()) {
			throw new IllegalArgumentException("Cuerpo de mensaje vacío"); //$NON-NLS-1$
		}
	}

}
