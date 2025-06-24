package lk.ijse.parkingspaceservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingReservationDTO {
    private String reservationId;
    private String licensePlate;;
    private String spaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
}