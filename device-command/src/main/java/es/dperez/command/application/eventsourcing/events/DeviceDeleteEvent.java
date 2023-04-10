package es.dperez.command.application.eventsourcing.events;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DeviceDeleteEvent {
  private UUID uuid;
  private String message;
}
