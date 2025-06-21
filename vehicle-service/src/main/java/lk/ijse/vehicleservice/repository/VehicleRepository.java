package lk.ijse.vehicleservice.repository;

import lk.ijse.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByUserId(Long userId);
    boolean existsByLicensePlate(String licensePlate);
}