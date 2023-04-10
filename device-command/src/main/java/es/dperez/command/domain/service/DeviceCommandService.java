package es.dperez.command.domain.service;

import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.domain.converter.DeviceConverter;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceCreatedEventSourcing;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceDeletedEventSourcing;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceUpdatedEventSourcing;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceCreatedEvent;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceDeleteEvent;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceUpdateEvent;
import es.dperez.command.infrasturcture.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeviceCommandService {

    private final DeviceConverter deviceConverter;
    private final DeviceRepository deviceRepository;
    private final KafkaDeviceCreatedEventSourcing kafkaDeviceCreatedEventSourcing;
    private final KafkaDeviceUpdatedEventSourcing kafkaDeviceUpdatedEventSourcing;
    private final KafkaDeviceDeletedEventSourcing kafkaDeviceDeletedEventSourcing;
    private final Logger logger = LoggerFactory.getLogger(DeviceCommandService.class);


    public DeviceCommandService(
        final DeviceConverter deviceConverter,
        final DeviceRepository deviceRepository,
        final KafkaDeviceCreatedEventSourcing kafkaDeviceCreatedEventSourcing,
        final KafkaDeviceUpdatedEventSourcing kafkaDeviceUpdatedEventSourcing,
        final KafkaDeviceDeletedEventSourcing kafkaDeviceDeletedEventSourcing) {
        this.deviceConverter = deviceConverter;
        this.deviceRepository = deviceRepository;
        this.kafkaDeviceCreatedEventSourcing = kafkaDeviceCreatedEventSourcing;
        this.kafkaDeviceUpdatedEventSourcing = kafkaDeviceUpdatedEventSourcing;
        this.kafkaDeviceDeletedEventSourcing = kafkaDeviceDeletedEventSourcing;
    }

    public DeviceCreatedEvent create(final CreateDeviceRequest request) throws JsonParsingException {
        logger.info("Creating new device: {}", request);
        final Device device = deviceConverter.createDeviceRequestToDevice(request);
        deviceRepository.save(device);
        return kafkaDeviceCreatedEventSourcing.publicCreateDeviceEvent(device);
    }

    public DeviceUpdateEvent update(final String name, final UpdateDeviceRequest request)
        throws DeviceNotFoundException, JsonParsingException {
        logger.info("Updating device: {}", name);
        final Device device = deviceRepository.findByName(name).orElseThrow(() -> new DeviceNotFoundException(name + "Device not found"));
        final Device updateDevice = deviceConverter.updateDeviceRequestToDevice(device, request);
        deviceRepository.save(updateDevice);
        return kafkaDeviceUpdatedEventSourcing.publicUpdateDeviceEvent(updateDevice);
    }

    public DeviceDeleteEvent delete(final String name) throws DeviceNotFoundException, JsonParsingException {
        logger.info("Deleting device: {}", name);
        final Device device = deviceRepository.findByName(name).orElseThrow(() -> new DeviceNotFoundException(name + "Device not found"));
        deviceRepository.delete(device);
        return kafkaDeviceDeletedEventSourcing.publicDeleteDeviceEvent(device);
    }
}
