@getBooking @regression
Feature: Validate Retrieve Booking Details

  Scenario: Successful retrieval of booking details
    When I send a GET request to booking endpoint with a valid booking id 1576
    Then The response status code should be 200
    And The response should include the necessary info

  Scenario: Unsuccessful retrieval of booking details
    When I send a GET request to booking endpoint with an invalid booking id "9655"
    Then The response status code should be 404 with "Not found" message

  Scenario: Successful retrieval of all booking IDs
    When I send a GET request to booking endpoint
    Then The response status code should be 200
    And the response should include ids

  Scenario Outline: Successful retrieval of all booking IDs by providing a specific parameter
    When I send a GET request to get bookings with "<paramType>" "<param>"
    Then The response should only show ids with "<paramType>" "<param>"


    Examples:
      | paramType       | param                |
      | firstname       | Josh                 |
      | firstname       | Jake                 |
      | firstname       | Jane                 |
      | additionalneeds | super bowls          |
      | additionalneeds | breakfast            |
      | additionalneeds | Extra pillows please |