package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {
    VehicleDTO registerVehicle(VehicleDTO vehicleDTO);
    VehicleDTO getVehicleById(Long id);
    List<VehicleDTO> getAllVehicles();
    List<VehicleDTO> getVehiclesByUser(Long userId);
    VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO);
    void deleteVehicle(Long id);
    boolean checkInVehicle(Long vehicleId, Long parkingSpaceId);
    boolean checkOutVehicle(Long vehicleId, Long parkingSpaceId);
}