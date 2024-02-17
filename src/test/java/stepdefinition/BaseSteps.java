package stepdefinition;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ConfigManager;

import static io.restassured.RestAssured.baseURI;

public class BaseSteps {

	protected static Response response;

	protected RequestSpecification request;

	protected ObjectMapper mapper = new ObjectMapper();

	protected String authEndpoint;

	protected String bookingEndpoint;

	protected static String token;

	public BaseSteps() {
		baseURI = ConfigManager.getProperty("base.uri");
		authEndpoint = ConfigManager.getProperty("api.auth.endpoint");
		bookingEndpoint = ConfigManager.getProperty("api.booking.endpoint");
	}

}
