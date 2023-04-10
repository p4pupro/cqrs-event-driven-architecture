package es.dperez.command.application.eventsourcing;

import es.dperez.command.application.eventsourcing.events.DeviceUpdateEvent;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;

public interface EventUpdatedPublisher {
    DeviceUpdateEvent publishDeviceUpdatedEvent(Device device) throws JsonParsingException;
}
