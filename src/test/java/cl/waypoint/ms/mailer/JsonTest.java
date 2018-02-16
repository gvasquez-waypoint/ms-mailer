package cl.waypoint.ms.mailer;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.waypoint.ms.mailer.dto.Message;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class JsonTest {

	private static final String URL = "http://localhost:8080/send";
	// private static final String URL =
	// "http://ec2-174-129-174-97.compute-1.amazonaws.com/send";

	@Test
	public void testRestrictions() throws Exception {
		PodamFactory factory = new PodamFactoryImpl();
		Message myPojo = factory.manufacturePojo(Message.class);
		System.out.println(myPojo);

		Message input = factory.manufacturePojo(Message.class);
		TestRestTemplate restTemplate = new TestRestTemplate();

		ObjectMapper mapper = new ObjectMapper();
		String body = mapper.writeValueAsString(input);
		System.out.println("Body:" + body);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		Object resp = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
		System.out.println("Response:" + resp);

	}

}
