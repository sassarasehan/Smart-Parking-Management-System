package lk.ijse.parkingspaceservice.repo;

import lk.ijse.parkingspaceservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSpaceRepo extends JpaRepository<ParkingSpace, String> {
    List<ParkingSpace> findByAvailable(boolean available);
    Optional<ParkingSpace> findFirstByAvailableTrue();
}