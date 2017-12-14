package cl.waypoint.ms.mailer.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.waypoint.ms.mailer.Mailer;
import cl.waypoint.ms.mailer.dto.Message;

@EnableAutoConfiguration
@RestController
public class MailerEndpoint {

	private static final Logger LOGGER = Logger.getLogger("MailerEndpoint");
	private static final String PONG = "pong";

	@RequestMapping("/send")
	public String send(@RequestBody Message msg) {
		LOGGER.log(Level.INFO, "Received: {0}", msg.toString());
		return Mailer.send(msg);
	}

	@RequestMapping("/ping")
	public String send() {
		return PONG;
	}

}
