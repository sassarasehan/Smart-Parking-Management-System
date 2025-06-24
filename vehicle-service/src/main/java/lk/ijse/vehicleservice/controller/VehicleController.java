package lk.ijse.vehicleservice.controller;

import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/register")
    public VehicleDTO registerVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.registerVehicle(vehicleDTO);
    }

    @GetMapping("/{id}")
    public VehicleDTO getVehicle(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/user/{userId}")
    public List<VehicleDTO> getVehiclesByUser(@PathVariable Long userId) {
        return vehicleService.getVehiclesByUser(userId);
    }

    @GetMapping("/getVehicleByNo/{vehicleNo}")
    public ResponseEntity<?> getByVehicleNo(@PathVariable String vehicleNo) {
        VehicleDTO dto = vehicleService.getVehicleByNo(vehicleNo);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vehicle with number '" + vehicleNo + "' not found");
        }
    }



    @PutMapping("/{id}")
    public VehicleDTO updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.updateVehicle(id, vehicleDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }

    @PostMapping("/check-in/{vehicleId}/{parkingSpaceId}")
    public boolean checkInVehicle(@PathVariable Long vehicleId, @PathVariable Long parkingSpaceId) {
        return vehicleService.checkInVehicle(vehicleId, parkingSpaceId);
    }

    @PostMapping("/check-out/{vehicleId}/{parkingSpaceId}")
    public boolean checkOutVehicle(@PathVariable Long vehicleId, @PathVariable Long parkingSpaceId) {
        return vehicleService.checkOutVehicle(vehicleId, parkingSpaceId);
    }
}