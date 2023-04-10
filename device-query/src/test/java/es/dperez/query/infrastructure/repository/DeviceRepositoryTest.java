package es.dperez.query.infrastructure.repository;

import es.dperez.query.domain.model.Device;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    void should_find_by_service_id() {
        // Given
        final String serviceId = "2128a214-f976-4ff3-a8c9-ebc529153449";
        Device device = Device.builder().serviceId(serviceId).build();
        when(deviceRepository.findByServiceId(serviceId)).thenReturn(Optional.of(device));

        // When
        Optional<Device> result = deviceRepository.findByServiceId(serviceId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getServiceId()).isEqualTo(serviceId);
    }

}

