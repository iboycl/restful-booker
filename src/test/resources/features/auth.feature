@auth @regression
Feature: Validate create token

  Scenario: Get a token with valid credentials
    Given I have authentication credentials "admin" and "password123"
    When I send a POST request to authentication endpoint
    Then The response status code should be 200
    And I receive a token in the response

  Scenario: Get a token with invalid credentials
    Given I have authentication credentials "admin1" and "password123"
    When I send a POST request to authentication endpoint
    Then The response status code should be 404 with "Bad credentials" message

  Scenario: Get a token with missing credentials
    Given I have authentication credentials "" and ""
    When I send a POST request to authentication endpoint
    Then The response status code should be 400 with "Bad credentials" message

  Scenario: Get a token with incorrect endpoint
    Given I have authentication credentials "admin" and "password123"
    When I send a POST request to booking endpoint
    Then The response status code should be 500

  Scenario: Create a token with empty request body
    When I send a POST request to authentication endpoint with an empty body
    Then The response status code should be 400 with "Bad credentials" message


  Scenario: Create a token with incorrect content-type header
    Given I have authentication credentials "admin" and "password123" with incorrect Content-Type
    When I send a POST request to authentication endpoint
    Then The response status code should be 415 with "Bad Request" message