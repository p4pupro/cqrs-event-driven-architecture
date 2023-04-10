package es.dperez.command.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateDeviceRequest {
  private String name;
  private String mark;
  private String model;
  private String color;
  private Double price;
}
