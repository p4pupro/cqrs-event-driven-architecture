package es.dperez.command.application.controller;

import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.application.exception.FindDeviceException;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.service.DeviceCommandService;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceCreatedEvent;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceDeleteEvent;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceUpdateEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DeviceCommandController {

    private final DeviceCommandService deviceCommandService;

    public DeviceCommandController(final DeviceCommandService deviceCommandService) {
        this.deviceCommandService = deviceCommandService;
    }

    @PostMapping("device")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DeviceCreatedEvent createDevice(@RequestBody final CreateDeviceRequest req) throws JsonParsingException {
        return deviceCommandService.create(req);
    }

    @PutMapping("device/{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DeviceUpdateEvent updateDevice(@PathVariable final String name, @RequestBody final UpdateDeviceRequest req)
        throws JsonParsingException {
        try {
            return deviceCommandService.update(name, req);
        } catch (DeviceNotFoundException ex) {
            throw new FindDeviceException("Not found device: " + name);
        }
    }

    @DeleteMapping("device/{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DeviceDeleteEvent deleteDevice(@PathVariable final String name) throws JsonParsingException {
        try {
            return deviceCommandService.delete(name);
        } catch (DeviceNotFoundException ex) {
            throw new FindDeviceException("Not found device: " + name);
        }
    }
}
