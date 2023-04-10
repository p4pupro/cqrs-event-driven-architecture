package es.dperez.command.application.eventsourcing;

import es.dperez.command.application.eventsourcing.events.DeviceCreatedEvent;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;


public interface EventCreatedPublisher {
    DeviceCreatedEvent publishDeviceCreatedEvent(Device device) throws JsonParsingException;
}
