package lk.ijse.parkingspaceservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ParkingReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reservationId;

    private String licensePlate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private ParkingSpace parkingSpace;
}