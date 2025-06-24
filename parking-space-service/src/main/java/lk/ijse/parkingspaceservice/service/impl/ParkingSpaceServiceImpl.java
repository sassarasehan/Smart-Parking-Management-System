package lk.ijse.parkingspaceservice.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.parkingspaceservice.repo.ParkingSpaceRepo;
import lk.ijse.parkingspaceservice.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    @Autowired
    private ParkingSpaceRepo repo;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String save(ParkingSpaceDTO dto) {
        ParkingSpace space = ParkingSpace.builder()
                .location(dto.getLocation())
                .available(true)
                .status("AVAILABLE")
                .build();
        return repo.save(space).getSpaceId();
    }

    @Override
    public List<ParkingSpaceDTO> getAll() {
        return repo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSpaceDTO> getAvailableSpaces() {
        return repo.findByAvailable(true).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String reserveSpace(String vehicleNo) {
        String url = "http://localhost:8082/vehicle-service/api/v1/vehicles/getVehicleByNo/" + vehicleNo;
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Vehicle not found");
        }

        Optional<ParkingSpace> optionalSpace = repo.findFirstByAvailableTrue();
        if (optionalSpace.isEmpty()) {
            throw new RuntimeException("No available parking spaces");
        }

        ParkingSpace space = optionalSpace.get();

        space.setLicensePlate(vehicleNo);
        space.setAvailable(false);
        space.setStatus("OCCUPIED");

        return repo.save(space).getSpaceId();
    }


    @Override
    public String releaseSpace(String spaceId) {
        System.out.println(spaceId);
        ParkingSpace space = repo.findById(spaceId).orElseThrow(() -> new RuntimeException("Space not found"));
        space.setAvailable(true);
        space.setStatus("AVAILABLE");
        return repo.save(space).getSpaceId();
    }

    private ParkingSpaceDTO mapToDTO(ParkingSpace space) {
        return ParkingSpaceDTO.builder()
                .spaceId(space.getSpaceId())
                .location(space.getLocation())
                .available(space.isAvailable())
                .status(space.getStatus())
                .build();
    }
}