package lk.ijse.parkingspaceservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String spaceId;

    private String location;
    private boolean available;

    @Column(unique = true)
    private String licensePlate;
    private String status;

}