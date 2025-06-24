package lk.ijse.parkingspaceservice.repo;

import lk.ijse.parkingspaceservice.entity.ParkingReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingReservationRepo extends JpaRepository<ParkingReservation, String> {
    List<ParkingReservation> findByLicensePlate(String licensePlate);
    List<ParkingReservation> findByActiveTrue();
}