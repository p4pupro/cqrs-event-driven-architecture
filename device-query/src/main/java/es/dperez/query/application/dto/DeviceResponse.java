package es.dperez.query.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceResponse {

    private String name;
    private String mark;
    private String model;
    private String color;
    private Double price;
}
