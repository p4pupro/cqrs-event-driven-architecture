package es.dperez.query.application.device;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.dperez.query.application.device.find.FindDevice;
import es.dperez.query.application.dto.DeviceResponse;
import es.dperez.query.application.dto.mapper.DeviceMapper;
import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.domain.model.Device;
import es.dperez.query.domain.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FindDeviceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private FindDevice findDevice;

    @Test
    void should_found_by_name_success() throws DeviceNotFoundException {
        // Given
        final String name = "Macbook";
        Device device = Device.builder().name(name).mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        DeviceResponse expectedResponse = DeviceResponse.builder().name(name).mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        when(deviceRepository.findByName(name)).thenReturn(Optional.of(device));
        when(deviceMapper.deviceToDeviceResponse(device)).thenReturn(expectedResponse);
        DeviceResponse actualResponse;

        // When
        actualResponse = findDevice.findByName(name);


        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(deviceRepository).findByName(name);
        verify(deviceMapper).deviceToDeviceResponse(device);
    }

    @Test
    void should_throw_device_not_found_when_find_by_name() {
        // Given
        String name = "Acer";

        // When // Then
        assertThrows(DeviceNotFoundException.class, () -> findDevice.findByName(name));
        verify(deviceRepository).findByName(name);
    }
}