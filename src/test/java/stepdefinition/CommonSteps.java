package stepdefinition;

import io.cucumber.java.en.Then;
import org.assertj.core.api.AutoCloseableBDDSoftAssertions;
import org.assertj.core.api.SoftAssertionError;

import static org.assertj.core.api.BDDAssertions.fail;
import static org.assertj.core.api.BDDAssertions.then;

public class CommonSteps extends BaseSteps {

	@Then("The response status code should be {int}")
	public void theResponseStatusCodeShouldBe(int httpStatusCode) {
		then(response.getStatusCode()).isEqualTo(httpStatusCode);
	}

	@Then("The response status code should be {int} with {string} message")
	public void theResponseStatusCodeShouldBeWithMessage(int httpStatusCode, String expectedMessage) {
		try (AutoCloseableBDDSoftAssertions soft = new AutoCloseableBDDSoftAssertions()) {

			soft.then(response.getStatusCode()).isEqualTo(httpStatusCode);

			String actualMessage;
			if (response.contentType().contains("json")) {
				actualMessage = response.jsonPath().getString("reason");
			}
			else
				actualMessage = response.asPrettyString();

			soft.then(actualMessage).isEqualTo(expectedMessage);
		} catch (SoftAssertionError e) {
			System.out.println(e.getMessage());
			fail("Status code or message is wrong!");
		}
	}

}
