package lk.ijse.parkingspaceservice.service;

import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;

import java.util.List;

public interface ParkingSpaceService {
    String save(ParkingSpaceDTO dto);
    List<ParkingSpaceDTO> getAll();
    List<ParkingSpaceDTO> getAvailableSpaces();
    String reserveSpace(String vehicleNo);
    String releaseSpace(String spaceId);
}