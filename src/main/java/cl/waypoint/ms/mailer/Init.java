package cl.waypoint.ms.mailer;

import java.util.Timer;

import org.springframework.boot.SpringApplication;

import cl.waypoint.ms.mailer.rest.MailerEndpoint;
import cl.waypoint.ms.mailer.timer.TimerBounce;

public class Init {

	private static final Timer bounceTimer = new Timer("bounceTimer");

	public static void main(String[] args) {
		bounceTimer.schedule(new TimerBounce(), 0L, 1000 * 60L);
		SpringApplication.run(MailerEndpoint.class, args);
	}

}
