package es.dperez.query.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "devices")
public class Device {

    @Id
    @JsonIgnore
    private String id;
    private String name;
    private String mark;
    private String model;
    private String color;
    private Double price;
    private String serviceId;
    private String creationDate;
    private String updatedDate;

    public Device toDomain(Device device, Device deviceWithUpdates) {
        device.setName(deviceWithUpdates.getName());
        device.setMark(deviceWithUpdates.getMark());
        device.setModel(deviceWithUpdates.getModel());
        device.setColor(deviceWithUpdates.getColor());
        device.setPrice(deviceWithUpdates.getPrice());
        device.setUpdatedDate(deviceWithUpdates.getUpdatedDate());
        return device;
    }
}
