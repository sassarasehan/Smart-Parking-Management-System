package lk.ijse.vehicleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private Long id;
    private String licensePlate;
    private String make;
    private String model;
    private String color;
    private String type;
    private Long userId;
}