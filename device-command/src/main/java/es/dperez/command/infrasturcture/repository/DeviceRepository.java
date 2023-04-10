package es.dperez.command.infrasturcture.repository;

import es.dperez.command.domain.model.Device;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Integer> {

  Optional<Device> findByName(String name);
}
