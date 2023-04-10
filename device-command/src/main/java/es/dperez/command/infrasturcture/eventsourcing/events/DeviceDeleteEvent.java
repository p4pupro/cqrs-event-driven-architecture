package es.dperez.command.infrasturcture.eventsourcing.events;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceDeleteEvent {
  private UUID uuid;
  private String message;
}
