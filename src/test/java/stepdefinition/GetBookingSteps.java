package stepdefinition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.AutoCloseableBDDSoftAssertions;
import org.assertj.core.api.SoftAssertionError;
import pojo.Booking;
import pojo.BookingId;


import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.BDDAssertions.fail;
import static org.assertj.core.api.BDDAssertions.then;

public class GetBookingSteps extends BaseSteps {

    private static final Logger LOGGER = LogManager.getLogger(GetBookingSteps.class);

    @When("I send a GET request to booking endpoint with a valid booking id {int}")
    public void iSendAGETRequestToBookingEndpointWithAValidBookingId(int id) {
        response = given().when().get(bookingEndpoint + "/" + id);
        LOGGER.debug("I send a get request with {}", id);
    }

    @And("The response should include the necessary info")
    public void theResponseShouldIncludeTheNecessaryInfo() {
        Booking actualBooking = response.getBody().as(Booking.class);
        try (AutoCloseableBDDSoftAssertions soft = new AutoCloseableBDDSoftAssertions()) {

            soft.then(actualBooking.getFirstname()).isNotEmpty().isNotNull();
            soft.then(actualBooking.getLastname()).isNotEmpty().isNotNull();
            soft.then(actualBooking.getTotalprice()).isNotNull();
            soft.then(actualBooking.isDepositpaid()).isTrue();
            soft.then(actualBooking.getBookingdates().getCheckin()).isNotEmpty().isNotNull();
            soft.then(actualBooking.getBookingdates().getCheckout()).isNotEmpty().isNotNull();
            soft.then(actualBooking.getAdditionalneeds()).isNotEmpty().isNotNull();
        } catch (SoftAssertionError e) {
            System.out.println(e.getMessage());
            fail("One of the fields is missing!");
        }
        LOGGER.debug("I verified any field is not empty or null");
    }

    @When("I send a GET request to booking endpoint with an invalid booking id {string}")
    public void iSendAGETRequestToBookingEndpointWithAInvalidBookingId(String id) {
        given().when().get(bookingEndpoint + "/" + id).then().statusCode(404);
        LOGGER.debug("I verified invalid id request status code is 404");
    }

    @When("I send a GET request to booking endpoint")
    public void iSendAGETRequestToBookingEndpoint() {
        response = given().when().get(bookingEndpoint);
        LOGGER.debug("I send a GET request to get booking ids");
    }

    @And("the response should include ids")
    public void theResponseShouldIncludeIds() {
        then(response.getBody().asPrettyString().contains("bookingid")).isTrue();
        LOGGER.debug("I verified response body contains 'bookingid'");
    }

    @When("I send a GET request to get bookings with {string} {string}")
    public void iSendAGETRequestToGetBookingsWithName(String paramType, String param) {
        response = given().when().get(bookingEndpoint + "?"+ paramType + "=" + param);
    }

    @Then("The response should only show ids with {string} {string}")
    public void theResponseShouldOnlyShowIdsWith(String paramType, String param) {
        String json = response.getBody().asPrettyString();
        List<BookingId> bookingIdList;
        try {
            bookingIdList = mapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        int index = (int) (Math.random()*bookingIdList.size());

        response = given().when().get(bookingEndpoint + "/" + index);
        Booking actualBooking = response.as(Booking.class);

        switch (paramType){
            case "firstname" -> then(actualBooking.getFirstname()).isEqualTo(param);
            case "lastname" -> then(actualBooking.getLastname()).isEqualTo(param);
            case "totalprice" -> then(actualBooking.getTotalprice()).isEqualTo(param);
            case "additionalneeds" -> then(actualBooking.getAdditionalneeds()).isEqualTo(param);
            case "checkin" -> then(actualBooking.getBookingdates().getCheckin()).isEqualTo(param);
            case "checkout" -> then(actualBooking.getBookingdates().getCheckout()).isEqualTo(param);
        }

    }

}
