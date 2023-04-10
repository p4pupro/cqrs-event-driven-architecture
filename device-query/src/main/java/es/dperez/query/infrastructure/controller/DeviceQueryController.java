package es.dperez.query.infrastructure.controller;

import es.dperez.query.application.dto.DeviceResponse;
import es.dperez.query.application.exception.FindDeviceException;
import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.application.service.DeviceQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceQueryController {

    private final DeviceQueryService deviceQueryService;

    public DeviceQueryController(DeviceQueryService deviceQueryService) {
        this.deviceQueryService = deviceQueryService;
    }

    @GetMapping("device/{name}")
    @ResponseStatus(HttpStatus.OK)
    public DeviceResponse findDevice(@PathVariable String name) {

        try {
            return deviceQueryService.findByName(name);
        } catch (DeviceNotFoundException ex) {
            throw new FindDeviceException("Not found device: " + name);
        }
    }
}
