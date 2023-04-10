package es.dperez.command.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import es.dperez.command.application.eventsourcing.events.DeviceCreatedEvent;
import es.dperez.command.application.eventsourcing.events.DeviceUpdateEvent;
import es.dperez.command.application.eventsourcing.events.DeviceDeleteEvent;
import es.dperez.command.infrasturcture.controller.DeviceCommandController;
import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.application.exception.FindDeviceException;
import es.dperez.command.domain.exception.JsonParsingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class CommandControllerSteps {

    @Autowired
    private DeviceCommandController controller;

    private CreateDeviceRequest createDeviceRequest;
    private UpdateDeviceRequest updateDeviceRequest;
    private ResponseEntity<?> response;
    private String deviceName;

    @Given("a create device request")
    public void aCreateDeviceRequest() {
        createDeviceRequest = CreateDeviceRequest.builder()
            .name("Macbook")
            .mark("Apple")
            .model("Pro M1 14inch")
            .color("Space Grey")
            .price(2250.99)
            .build();
    }

    @When("the request is sent to the create device endpoint")
    public void theRequestIsSentToTheCreateDeviceEndpoint() throws JsonParsingException {
        DeviceCreatedEvent createdEvent = controller.createDevice(createDeviceRequest);
        response = new ResponseEntity<>(createdEvent, HttpStatus.ACCEPTED);
    }

    @Then("the create response should have an HTTP status code of {int}")
    public void the_create_response_should_have_an_http_status_code_of(Integer statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), response.getStatusCode());
    }

    @Then("the response should contain a device created event")
    public void the_response_should_contain_a_device_created_event() {
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof DeviceCreatedEvent);
        DeviceCreatedEvent createEvent = (DeviceCreatedEvent) response.getBody();
        assertEquals(createDeviceRequest.getName(), createEvent.getDevice().getName());
        assertEquals(createDeviceRequest.getMark(), createEvent.getDevice().getMark());
        assertEquals(createDeviceRequest.getModel(), createEvent.getDevice().getModel());
        assertEquals(createDeviceRequest.getColor(), createEvent.getDevice().getColor());
        assertEquals(createDeviceRequest.getPrice(), createEvent.getDevice().getPrice());
    }

    @Given("an update device request with a valid device name")
    public void anUpdateDeviceRequestWithAValidDeviceName() {
        updateDeviceRequest = UpdateDeviceRequest.builder()
            .name("Macbook")
            .mark("Apple")
            .model("Pro M2 16inch")
            .color("Silver")
            .price(3350.99)
            .build();
        deviceName = "Macbook";
    }

    @When("the request is sent to the update device endpoint")
    public void theRequestIsSentToTheUpdateDeviceEndpoint() {
        try {
            System.out.println("HOLA: " + deviceName);
            DeviceUpdateEvent updateEvent = controller.updateDevice(deviceName, updateDeviceRequest);
            response = new ResponseEntity<>(updateEvent, HttpStatus.ACCEPTED);
        } catch (FindDeviceException | JsonParsingException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Then("the update response should have an HTTP status code of {int}")
    public void the_update_response_should_have_an_http_status_code_of(Integer statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), response.getStatusCode());
    }

    @Then("the response should contain a device update event")
    public void the_response_should_contain_a_device_update_event() {
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof DeviceUpdateEvent);
        DeviceUpdateEvent updateEvent = (DeviceUpdateEvent) response.getBody();
        assertEquals(updateDeviceRequest.getName(), updateEvent.getDevice().getName());
        assertEquals(updateDeviceRequest.getMark(), updateEvent.getDevice().getMark());
        assertEquals(updateDeviceRequest.getModel(), updateEvent.getDevice().getModel());
        assertEquals(updateDeviceRequest.getColor(), updateEvent.getDevice().getColor());
        assertEquals(updateDeviceRequest.getPrice(), updateEvent.getDevice().getPrice());
    }

    @Given("a valid device name")
    public void a_valid_device_name() {
        deviceName = "Macbook";
    }

    @When("the request is sent to the delete device endpoint")
    public void the_request_is_sent_to_the_delete_device_endpoint() {
        try {
            DeviceDeleteEvent deleteEvent = controller.deleteDevice(deviceName);
            response = new ResponseEntity<>(deleteEvent, HttpStatus.ACCEPTED);
        } catch (FindDeviceException | JsonParsingException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Then("the delete response should have an HTTP status code of {int}")
    public void the_delete_response_should_have_an_http_status_code_of(Integer statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), response.getStatusCode());
    }

    @Then("the response should contain a device delete event")
    public void the_response_should_contain_a_device_delete_event() {
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof DeviceDeleteEvent);
    }

}
