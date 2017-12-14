package cl.waypoint.ms.mailer.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.waypoint.ms.mailer.Mailer;
import cl.waypoint.ms.mailer.dto.Message;

@EnableAutoConfiguration
@RestController
@Validated
public class MailerEndpoint {

	private static final Logger LOGGER = Logger.getLogger("MailerEndpoint");
	private static final String PONG = "pong";

	@RequestMapping("/send")
	public RestMessage send(@Valid @RequestBody Message msg) {
		LOGGER.log(Level.INFO, "Received: {0}", msg);
		return Mailer.send(msg);
	}

	@RequestMapping("/ping")
	public String send() {
		return PONG;
	}

}
