Feature: Command Controller API

  Scenario: Create a device
    Given a create device request
    When the request is sent to the create device endpoint
    Then the create response should have an HTTP status code of 202
    And the response should contain a device created event

  Scenario: Update a device
    Given an update device request with a valid device name
    When the request is sent to the update device endpoint
    Then the update response should have an HTTP status code of 202
    And the response should contain a device update event

  Scenario: Delete a device
    Given a valid device name
    When the request is sent to the delete device endpoint
    Then the delete response should have an HTTP status code of 202
    And the response should contain a device delete event