package es.dperez.query.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dperez.query.application.controller.DeviceQueryController;
import es.dperez.query.application.dto.DeviceResponse;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class QueryControllerSteps {

    @Autowired
    private DeviceQueryController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    private String deviceName;
    private ResponseEntity<DeviceResponse> response;

    @Given("a device with name {string}")
    public void a_device_with_name(String name) {
        deviceName = name;
    }

    @Before
    public void createDevice() {
        String createDeviceUrl = "http://localhost:8081/device";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Macbook");
        requestBody.put("mark", "Apple");
        requestBody.put("model", "Pro M1 14inch");
        requestBody.put("color", "Space Grey");
        requestBody.put("price", 2250.99);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody;

        try {
            jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing object to JSON", e);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);

        restTemplate.postForEntity(createDeviceUrl, requestEntity, Void.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while waiting for test", e);
        }
    }

    @After
    public void deleteDevice() {
        String createDeviceUrl = "http://localhost:8081/device/Macbook";
        restTemplate.delete(createDeviceUrl);
    }

    @When("the request is sent to the find device endpoint with the name {string}")
    public void the_request_is_sent_to_the_find_device_endpoint_with_name(String name) {
        response = new ResponseEntity<>(controller.findDevice(name), HttpStatus.OK);
    }

    @Then("the response should have an HTTP status code of {int}")
    public void the_response_should_have_an_http_status_code_of(Integer expectedStatusCode) {
        assertEquals(expectedStatusCode.intValue(), response.getStatusCodeValue());
    }

    @Then("the response should contain the device information")
    public void the_response_should_contain_the_device_information() {
        DeviceResponse deviceResponse = response.getBody();
        assertNotNull(deviceResponse);
        assertEquals(deviceName, deviceResponse.getName());
    }
}


