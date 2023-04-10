package es.dperez.query.application.dto.mapper;

import es.dperez.query.application.dto.DeviceResponse;
import es.dperez.query.domain.model.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public DeviceResponse deviceToDeviceResponse(Device p) {
        return DeviceResponse.builder()
            .name(p.getName())
            .mark(p.getMark())
            .model(p.getModel())
            .color(p.getColor())
            .price(p.getPrice())
            .build();
    }
}
