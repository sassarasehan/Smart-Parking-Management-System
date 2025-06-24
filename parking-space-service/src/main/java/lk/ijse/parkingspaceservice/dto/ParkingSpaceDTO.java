package lk.ijse.parkingspaceservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingSpaceDTO {
    private String spaceId;
    private String location;
    private boolean available;
    private String licensePlate;;
    private String status;
}