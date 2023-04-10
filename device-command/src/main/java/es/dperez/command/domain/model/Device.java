package es.dperez.command.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "devices")
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

}
