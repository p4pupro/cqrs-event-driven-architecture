package es.dperez.query.domain.converter;

import es.dperez.query.application.dto.DeviceResponse;
import es.dperez.query.domain.model.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceConverter {

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
