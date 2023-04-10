package es.dperez.command.application.service.device;

import es.dperez.command.application.device.create.CreateDevice;
import es.dperez.command.application.device.delete.DeleteDevice;
import es.dperez.command.application.device.update.UpdateDevice;
import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.application.eventsourcing.events.DeviceCreatedEvent;
import es.dperez.command.application.eventsourcing.events.DeviceDeleteEvent;
import es.dperez.command.application.eventsourcing.events.DeviceUpdateEvent;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeviceCommandService {

    private final CreateDevice createDevice;
    private final UpdateDevice updateDevice;
    private final DeleteDevice deleteDevice;
    private final Logger logger = LoggerFactory.getLogger(DeviceCommandService.class);


    public DeviceCommandService(
        final CreateDevice createDevice,
        final UpdateDevice updateDevice,
        final DeleteDevice deleteDevice) {

        this.createDevice = createDevice;
        this.updateDevice = updateDevice;
        this.deleteDevice = deleteDevice;
    }

    public DeviceCreatedEvent createDevice(final CreateDeviceRequest request) throws JsonParsingException {
        logger.info("Creating device: {}", request.getName());
        return createDevice.create(request);
    }

    public DeviceUpdateEvent updateDevice(final String name, final UpdateDeviceRequest request)
        throws DeviceNotFoundException, JsonParsingException {
        logger.info("Updating device: {}", name);
        return updateDevice.update(name, request);
    }

    public DeviceDeleteEvent deleteDevice(final String name) throws DeviceNotFoundException, JsonParsingException {
        logger.info("Deleting device: {}", name);
        return deleteDevice.delete(name);
    }
}
