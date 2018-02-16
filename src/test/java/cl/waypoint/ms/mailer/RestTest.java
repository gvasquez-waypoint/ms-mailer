package cl.waypoint.ms.mailer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cl.waypoint.ms.mailer.dto.Message;
import cl.waypoint.ms.mailer.rest.MailerEndpoint;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MailerEndpoint.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTest {
	private static final PodamFactory factory = new PodamFactoryImpl();

	@Value("${local.server.port}")
	int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}

	@Test
	public void testServerPort() {
		assertNotEquals(0, port);
	}

	@Test
	public void testPing() {
		RestAssured.when().get("/ping").then().statusCode(200).contentType(ContentType.TEXT).body(equalTo("pong"));
	}

	@Test
	public void testInbound() {
		Message msg = factory.manufacturePojo(Message.class);
		Message[] jsonInput = { msg };
		RestAssured.given().contentType(ContentType.JSON).body(jsonInput).when().post("/data").then().statusCode(200)
				.contentType(ContentType.TEXT).body(equalTo("pong"));
	}

	@Test
	public void testConstraintViolation() {
		Message msg = factory.manufacturePojo(Message.class);
		Message[] jsonInput = { msg };
		RestAssured.given().contentType(ContentType.JSON).body(jsonInput).when().post("/data").then().statusCode(400)
				.contentType(ContentType.JSON).body("status", equalTo(400)).body("detail", notNullValue());
	}

	@Test
	public void testNotPost() {
		RestAssured.get("/data").then().statusCode(405).contentType(ContentType.JSON).body("status", equalTo(405))
				.body("detail", nullValue());
	}

	@Test
	public void tesNotJsonInput() {
		RestAssured.post("/data").then().statusCode(400).body("status", equalTo(400));
	}

	@Test
	public void testNoBody() {
		RestAssured.given().contentType(ContentType.JSON).post("/data").then().statusCode(400).body("status",
				equalTo(400));
	}

}
