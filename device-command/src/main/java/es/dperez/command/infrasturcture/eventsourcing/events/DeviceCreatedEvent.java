package es.dperez.command.infrasturcture.eventsourcing.events;

import es.dperez.command.domain.model.Device;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DeviceCreatedEvent {
    private UUID uuid;
    private Device device;
    private String message;
}
