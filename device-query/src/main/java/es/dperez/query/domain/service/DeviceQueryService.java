package es.dperez.query.domain.service;

import es.dperez.query.application.dto.DeviceResponse;
import es.dperez.query.domain.converter.DeviceConverter;
import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.domain.model.Device;
import es.dperez.query.infrastructure.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeviceQueryService {

    private final Logger logger = LoggerFactory.getLogger(DeviceQueryService.class);

    private final DeviceRepository deviceRepository;

    private final DeviceConverter deviceConverter;

    private static final String DEVICE_NOT_FOUND = "Device not found";

    public DeviceQueryService(final DeviceRepository deviceRepository,
        final DeviceConverter deviceConverter) {
        this.deviceRepository = deviceRepository;
        this.deviceConverter = deviceConverter;
    }

    public DeviceResponse findByName(final String name) throws DeviceNotFoundException {
        final Device device = deviceRepository.findByName(name).orElseThrow(() -> new DeviceNotFoundException(name, DEVICE_NOT_FOUND));
        logger.info("Find device: {}", device);
        return deviceConverter.deviceToDeviceResponse(device);
    }

    public void createDevice(final Device p) {
        logger.info("Insert new device: {}", p);
        deviceRepository.save(p);
    }

    public void updateDevice(final Device deviceWithUpdates) throws DeviceNotFoundException {
        final Device device = deviceRepository.findByServiceId(deviceWithUpdates.getServiceId())
            .orElseThrow(() -> new DeviceNotFoundException(deviceWithUpdates.getServiceId(), DEVICE_NOT_FOUND));
        final Device deviceUpdated = device.toDomain(device, deviceWithUpdates);
        logger.info("Updated device: {}", deviceUpdated);
        deviceRepository.save(deviceUpdated);
    }

    public void deleteDevice(final Device deviceToDelete) throws DeviceNotFoundException {
        final Device device = deviceRepository.findByServiceId(deviceToDelete.getServiceId())
            .orElseThrow(() -> new DeviceNotFoundException(deviceToDelete.getServiceId(), DEVICE_NOT_FOUND));
        logger.info("Delete device: {}", device);
        deviceRepository.delete(device);
    }
}
