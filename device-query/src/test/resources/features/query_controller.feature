Feature: Query Controller API

  Scenario: Find a device by name
    Given a device with name "Macbook"
    When the request is sent to the find device endpoint with the name "Macbook"
    Then the response should have an HTTP status code of 200
    And the response should contain the device information