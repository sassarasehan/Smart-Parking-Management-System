package lk.ijse.vehicleservice.service.impl;

import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.repository.VehicleRepository;
import lk.ijse.vehicleservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VehicleRepository vehicleRepository;

    private VehicleDTO mapToDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getType(),
                vehicle.getUserId()
        );
    }

    private Vehicle mapToEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(dto.getId());
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setMake(dto.getMake());
        vehicle.setModel(dto.getModel());
        vehicle.setColor(dto.getColor());
        vehicle.setType(dto.getType());
        vehicle.setUserId(dto.getUserId());
        return vehicle;
    }
    @Override
    public VehicleDTO registerVehicle(VehicleDTO vehicleDTO) {
        String userServiceUrl = "http://localhost:8081/user-service/api/v1/users/getUserByID/" + vehicleDTO.getUserId();

        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(userServiceUrl, Object.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                if (vehicleRepository.existsByLicensePlate(vehicleDTO.getLicensePlate())) {
                    throw new RuntimeException("Vehicle with this license plate already exists");
                }
                Vehicle vehicle = mapToEntity(vehicleDTO);
                return mapToDTO(vehicleRepository.save(vehicle));
            } else {
                throw new RuntimeException("User not defined: " + vehicleDTO.getUserId());
            }

        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User not defined: " + vehicleDTO.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("Error checking user: " + e.getMessage());
        }
    }


    @Override
    public VehicleDTO getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDTO> getVehiclesByUser(Long userId) {
        return vehicleRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO getVehicleByNo(String vehicleNo) {
        return vehicleRepository.findByLicensePlate(vehicleNo)
                .map(this::mapToDTO)
                .orElse(null);
    }


    @Override
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existingVehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        existingVehicle.setMake(vehicleDTO.getMake());
        existingVehicle.setModel(vehicleDTO.getModel());
        existingVehicle.setColor(vehicleDTO.getColor());
        existingVehicle.setType(vehicleDTO.getType());
        existingVehicle.setUserId(vehicleDTO.getUserId());

        return mapToDTO(vehicleRepository.save(existingVehicle));
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    @Override
    public boolean checkInVehicle(Long vehicleId, Long parkingSpaceId) {
        return true;
    }

    @Override
    public boolean checkOutVehicle(Long vehicleId, Long parkingSpaceId) {
        return true;
    }
}
