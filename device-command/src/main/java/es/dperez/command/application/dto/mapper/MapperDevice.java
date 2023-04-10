package es.dperez.command.application.dto.mapper;

import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.domain.model.Device;
import java.util.UUID;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperDevice {

    public Device createDeviceRequestToDevice(CreateDeviceRequest req) {
        return Device.builder()
                .id(UUID.randomUUID().toString())
                .color(req.getColor())
                .serviceId(UUID.randomUUID().toString())
                .model(req.getModel())
                .name(req.getName())
                .mark(req.getMark())
                .price(req.getPrice())
                .creationDate(LocalDateTime.now().toString())
                .updatedDate(null)
                .build();
    }

    public Device updateDeviceRequestToDevice(Device device, UpdateDeviceRequest req) {
        device.setName(req.getName());
        device.setModel(req.getModel());
        device.setMark(req.getMark());
        device.setColor(req.getColor());
        device.setPrice(req.getPrice());
        device.setUpdatedDate(LocalDateTime.now().toString());
        return device;
    }
}
