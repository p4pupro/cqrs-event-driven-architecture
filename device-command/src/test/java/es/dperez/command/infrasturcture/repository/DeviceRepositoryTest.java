package es.dperez.command.infrasturcture.repository;

import es.dperez.command.domain.model.Device;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DeviceRepositoryTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Test
    void should_find_by_name() {
        // Given
        final String name = "Macbook";
        Device device = Device.builder().name(name).build();
        device.setName(name);
        when(deviceRepository.findByName(name)).thenReturn(Optional.of(device));

        // When
        Optional<Device> result = deviceRepository.findByName(name);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(name);
    }

    @Test
    void should_return_empty_optional_if_not_exist() {
        // Given
        final String name = "Acer";
        when(deviceRepository.findByName(name)).thenReturn(Optional.empty());

        // When
        Optional<Device> result = deviceRepository.findByName(name);

        // Then
        assertFalse(result.isPresent());
    }
}
