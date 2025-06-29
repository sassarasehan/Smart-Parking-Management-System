package lk.ijse.vehicleservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vehicles")
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String licensePlate;
    private String make;
    private String model;
    private String color;
    private String type;
    
    @Column(name = "user_id")
    private Long userId;
}