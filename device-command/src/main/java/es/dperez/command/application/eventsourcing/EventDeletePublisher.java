package es.dperez.command.application.eventsourcing;

import es.dperez.command.application.eventsourcing.events.DeviceDeleteEvent;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;

public interface EventDeletePublisher {
    DeviceDeleteEvent publishDeviceDeleteEvent(Device device) throws JsonParsingException;
}
