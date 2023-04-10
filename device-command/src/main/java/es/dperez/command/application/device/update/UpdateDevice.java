package es.dperez.command.application.device.update;

import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.application.dto.mapper.MapperDevice;
import es.dperez.command.application.eventsourcing.EventUpdatedPublisher;
import es.dperez.command.application.eventsourcing.events.DeviceUpdateEvent;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.domain.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateDevice {

    private final MapperDevice mapperDevice;
    private final DeviceRepository deviceRepository;
    private final EventUpdatedPublisher eventUpdatedPublisher;
    private final Logger logger = LoggerFactory.getLogger(UpdateDevice.class);

    public UpdateDevice(
            final MapperDevice mapperDevice,
            final DeviceRepository deviceRepository,
            final EventUpdatedPublisher eventUpdatedPublisher) {
        this.mapperDevice = mapperDevice;
        this.deviceRepository = deviceRepository;
        this.eventUpdatedPublisher = eventUpdatedPublisher;
    }

    public DeviceUpdateEvent update(final String name, final UpdateDeviceRequest request)
            throws DeviceNotFoundException, JsonParsingException {
        logger.info("Updating device: {}", name);
        final Device device = deviceRepository.findByName(name).orElseThrow(() -> new DeviceNotFoundException(name + "Device not found"));
        final Device updateDevice = mapperDevice.updateDeviceRequestToDevice(device, request);
        deviceRepository.save(updateDevice);
        return eventUpdatedPublisher.publishDeviceUpdatedEvent(updateDevice);
    }
}
