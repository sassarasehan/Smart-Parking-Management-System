package lk.ijse.parkingspaceservice.service.impl;

import lk.ijse.parkingspaceservice.dto.ParkingReservationDTO;
import lk.ijse.parkingspaceservice.entity.ParkingReservation;
import lk.ijse.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.parkingspaceservice.repo.ParkingReservationRepo;
import lk.ijse.parkingspaceservice.repo.ParkingSpaceRepo;
import lk.ijse.parkingspaceservice.service.ParkingReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class ParkingReservationServiceImpl implements ParkingReservationService {

    @Autowired
    private ParkingReservationRepo reservationRepo;

    @Autowired
    private ParkingSpaceRepo spaceRepo;


    @Override
    public String createReservation(String vehicleNo, String spaceId) {
        ParkingSpace space = spaceRepo.findById(spaceId)
                .orElseThrow(() -> new RuntimeException("Space not found"));

        System.out.println(space + " isHere");

        ParkingReservation reservation = ParkingReservation.builder()
                .parkingSpace(space)
                .licensePlate(vehicleNo)
                .startTime(LocalDateTime.now())
                .active(true)
                .build();

        ParkingReservation saved = reservationRepo.save(reservation);
        System.out.println("Saved reservation: " + saved);

        return saved.getReservationId();
    }



    @Override
    public String endReservation(String reservationId) {
        ParkingReservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setEndTime(LocalDateTime.now());
        reservation.setActive(false);
        reservationRepo.save(reservation);

        // Free the parking space
        ParkingSpace space = reservation.getParkingSpace();
        space.setAvailable(true);
        space.setStatus("AVAILABLE");
        spaceRepo.save(space);

        return space.getSpaceId();
    }

    @Override
    public List<ParkingReservationDTO> getAllReservations() {
        return reservationRepo.findAll().stream().map(res ->
                ParkingReservationDTO.builder()
                .reservationId(res.getReservationId())
                .licensePlate(res.getLicensePlate())
                .spaceId(res.getParkingSpace().getSpaceId())
                .startTime(res.getStartTime())
                .endTime(res.getEndTime())
                .active(res.isActive())
                .build()).collect(Collectors.toList());
    }
    @Override
    public ParkingReservationDTO getActiveReservationDTOByVehicleNo(String vehicleNo) {
        ParkingReservation reservation = reservationRepo.findByLicensePlate(vehicleNo).stream()
                .filter(ParkingReservation::isActive)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active reservation found for vehicle: " + vehicleNo));

        return new ParkingReservationDTO(
                reservation.getReservationId(),
                reservation.getLicensePlate(),
                reservation.getParkingSpace().getSpaceId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.isActive()
        );
    }

    @Override
    public String getActiveReservationByVehicleNo(String vehicleNo) {
        return reservationRepo.findByLicensePlate(vehicleNo).stream()
                .filter(ParkingReservation::isActive)
                .findFirst()
                .map(ParkingReservation::getReservationId)
                .orElseThrow(() -> new RuntimeException("No active reservation found for vehicle: " + vehicleNo));
    }


}