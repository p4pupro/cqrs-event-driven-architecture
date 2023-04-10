package es.dperez.query.domain.repository;

import es.dperez.query.domain.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface DeviceRepository extends MongoRepository<Device, Integer> {

    Optional<Device> findByName(String name);

    Optional<Device> findByServiceId(String serviceId);
}
