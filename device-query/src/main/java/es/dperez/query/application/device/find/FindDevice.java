package es.dperez.query.application.device.find;

import es.dperez.query.application.dto.DeviceResponse;
import es.dperez.query.application.dto.mapper.DeviceMapper;
import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.domain.model.Device;
import es.dperez.query.domain.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class FindDevice {

    private final Logger logger = LoggerFactory.getLogger(FindDevice.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    private static final String DEVICE_NOT_FOUND = "Device not found";

    public FindDevice(final DeviceRepository deviceRepository,
                              final DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    public DeviceResponse findByName(final String name) throws DeviceNotFoundException {
        final Device device = deviceRepository.findByName(name).orElseThrow(() -> new DeviceNotFoundException(name, DEVICE_NOT_FOUND));
        logger.info("Find device: {}", device);
        return deviceMapper.deviceToDeviceResponse(device);
    }
}