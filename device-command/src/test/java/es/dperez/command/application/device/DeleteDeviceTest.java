package es.dperez.command.application.device;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.dperez.command.application.device.delete.DeleteDevice;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.domain.repository.DeviceRepository;
import java.util.Optional;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceDeletedImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteDeviceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private KafkaDeviceDeletedImpl kafkaDeviceDeletedImpl;

    @InjectMocks
    private DeleteDevice deleteDevice;

    @Test
    void should_delete_device() throws DeviceNotFoundException, JsonParsingException {
        // Given
        final String name = "Macbook";
        Device device = Device.builder().name(name).mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        when(deviceRepository.findByName(name)).thenReturn(Optional.of(device));

        // When
        deleteDevice.delete(name);

        // Then
        verify(deviceRepository).delete(device);
        verify(kafkaDeviceDeletedImpl).publishDeviceDeleteEvent(device);
    }

    @Test
    void should_throw_device_not_found_when_delete() {
        // Given
        String name = "Acer";

        // When
        when(deviceRepository.findByName(name)).thenReturn(Optional.empty());

        // Then
        assertThrows(DeviceNotFoundException.class, () -> deleteDevice.delete(name));
    }
}
