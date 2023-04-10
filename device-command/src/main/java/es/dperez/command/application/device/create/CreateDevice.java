package es.dperez.command.application.device.create;

import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.mapper.MapperDevice;
import es.dperez.command.application.eventsourcing.events.DeviceCreatedEvent;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.application.eventsourcing.EventCreatedPublisher;
import es.dperez.command.domain.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class CreateDevice {

    private final MapperDevice mapperDevice;
    private final DeviceRepository deviceRepository;
    private final EventCreatedPublisher eventCreatedPublisher;
    private final Logger logger = LoggerFactory.getLogger(CreateDevice.class);

    public CreateDevice(
            final MapperDevice mapperDevice,
            final DeviceRepository deviceRepository,
            final EventCreatedPublisher eventCreatedPublisher) {
        this.mapperDevice = mapperDevice;
        this.deviceRepository = deviceRepository;
        this.eventCreatedPublisher = eventCreatedPublisher;
    }

    public DeviceCreatedEvent create(final CreateDeviceRequest request) throws JsonParsingException {
        logger.info("Creating new device: {}", request);
        final Device device = mapperDevice.createDeviceRequestToDevice(request);
        deviceRepository.save(device);
        return eventCreatedPublisher.publishDeviceCreatedEvent(device);
    }
}
