package es.dperez.command.application.device.delete;

import es.dperez.command.application.eventsourcing.EventDeletePublisher;
import es.dperez.command.application.eventsourcing.events.DeviceDeleteEvent;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.domain.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeleteDevice {
    private final DeviceRepository deviceRepository;
    private final EventDeletePublisher eventDeletePublisher;
    private final Logger logger = LoggerFactory.getLogger(DeleteDevice.class);


    public DeleteDevice(
            final DeviceRepository deviceRepository,
            final EventDeletePublisher eventDeletePublisher) {
        this.deviceRepository = deviceRepository;
        this.eventDeletePublisher = eventDeletePublisher;
    }

    public DeviceDeleteEvent delete(final String name) throws DeviceNotFoundException, JsonParsingException {
        logger.info("Deleting device: {}", name);
        final Device device = deviceRepository.findByName(name).orElseThrow(() -> new DeviceNotFoundException(name + "Device not found"));
        deviceRepository.delete(device);
        return eventDeletePublisher.publishDeviceDeleteEvent(device);
    }
}
