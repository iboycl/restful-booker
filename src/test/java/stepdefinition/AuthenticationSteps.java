package stepdefinition;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojo.LoginCredentials;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.BDDAssertions.then;

public class AuthenticationSteps extends BaseSteps {

	private static final Logger LOGGER = LogManager.getLogger(AuthenticationSteps.class);

	@Given("I have authentication credentials {string} and {string}")
	public void iHaveAuthenticationCredentialsAnd(String username, String password) {
		LoginCredentials credentials = new LoginCredentials(username, password);
		request = given().contentType("application/json").body(credentials);
		LOGGER.info("Authentication credentials set for user: {}", username);
	}

	@When("I send a POST request to authentication endpoint")
	public void iSendAPOSTRequestToAuthenticationEndpoint() {
		response = request.when().post(authEndpoint);
		LOGGER.debug("POST request sent to {}", authEndpoint);
	}

	@And("I receive a token in the response")
	public void iReceiveATokenInTheResponse() {
		token = response.jsonPath().getString("token");
		then(token).as("Token is empty or null!").isNotEmpty().isNotNull();
	}

	@When("I send a POST request to booking endpoint")
	public void iSendAPOSTRequestToBookingEndpoint() {
		response = request.when().post(bookingEndpoint);
		LOGGER.debug("POST request sent to {}", bookingEndpoint);
	}

	@When("I send a POST request to authentication endpoint with an empty body")
	public void iSendAPOSTRequestToAuthenticationEndpointWithAnEmptyBody() {
		request = given().contentType("application/json");
		response = request.when().post(authEndpoint);
		LOGGER.debug("POST request sent to {} with empty body", authEndpoint);
	}

	@Given("I have authentication credentials {string} and {string} with incorrect Content-Type")
	public void iHaveAuthenticationCredentialsAndWithIncorrectContentType(String username, String password) {
		LoginCredentials credentials = new LoginCredentials(username, password);
		try {
			String xmlFormat = mapper.writeValueAsString(credentials);
			request = given().contentType("application/xml").body(xmlFormat);
			LOGGER.info("Authentication credentials set for user: {}", username);
		}
		catch (JsonProcessingException e) {
			LOGGER.error("Error serializing registration credentials", e);
			throw new RuntimeException(e);
		}
	}


}
