package stepdefinition.hook;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertionError;
import stepdefinition.BaseSteps;

public class Hooks extends BaseSteps {

	private static final Logger LOGGER = LogManager.getLogger(Hooks.class);

	@Before
	public void setUp(Scenario scenario) {
		LOGGER.info("::::::::::::::: TEST INFORMARION :::::::::::::::");
		LOGGER.info("Executing scenario: " + scenario.getName());
	}

	@After
	public void tearDown(Scenario scenario) {

		if (scenario.isFailed()) {
			if (response != null) {
				LOGGER.error("Scenario failed! Logging response body for failed scenario: " + scenario.getName());
				LOGGER.error(response.getBody().prettyPrint());
			}
			else {
				LOGGER.error("Scenario failed! But no response was set in the TestContext.");
			}
		}

		LOGGER.info("Finished scenario: " + scenario.getName());
		LOGGER.info("::::::::::::::::::::::::::::::::::::::::::::::::");
	}

}
