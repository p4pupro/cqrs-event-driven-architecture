package es.dperez.command.infrasturcture.eventsourcing.events;

import es.dperez.command.domain.model.Device;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceUpdateEvent {
  private UUID uuid;
  private Device device;
  private String message;
}
