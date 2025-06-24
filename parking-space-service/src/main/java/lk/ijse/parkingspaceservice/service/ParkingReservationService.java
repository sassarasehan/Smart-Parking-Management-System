package lk.ijse.parkingspaceservice.service;

import lk.ijse.parkingspaceservice.dto.ParkingReservationDTO;

import java.util.List;

public interface ParkingReservationService {
    String createReservation(String vehicleNo, String spaceId);
    String endReservation(String reservationId);
    List<ParkingReservationDTO> getAllReservations();

    String getActiveReservationByVehicleNo(String vehicleNo);

    ParkingReservationDTO getActiveReservationDTOByVehicleNo(String vehicleNo);
}