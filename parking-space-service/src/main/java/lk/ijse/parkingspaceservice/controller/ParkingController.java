/*
package lk.ijse.parkingspaceservice.controller;

import lk.ijse.parkingspaceservice.dto.ParkingReservationDTO;
import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingspaceservice.entity.ParkingReservation;
import lk.ijse.parkingspaceservice.service.ParkingReservationService;
import lk.ijse.parkingspaceservice.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private ParkingReservationService reservationService;

    @PostMapping("/spaces/save")
    public ResponseEntity<String> saveParkingSpace(@RequestBody ParkingSpaceDTO dto) {
        return ResponseEntity.ok(parkingSpaceService.save(dto));
    }

    @GetMapping("/spaces/getAll")
    public ResponseEntity<List<ParkingSpaceDTO>> getAllSpaces() {
        return ResponseEntity.ok(parkingSpaceService.getAll());
    }

    @GetMapping("/spaces/available")
    public ResponseEntity<List<ParkingSpaceDTO>> getAvailableSpaces() {
        return ResponseEntity.ok(parkingSpaceService.getAvailableSpaces());
    }

    @PutMapping("/spaces/reserve/{vehicleNo}")
    public ResponseEntity<?> reserveSpaceAndCreateReservation(@PathVariable String vehicleNo) {
        try {
            String spaceId = parkingSpaceService.reserveSpace(vehicleNo);

            String reservationId = reservationService.createReservation(vehicleNo, spaceId);

            return ResponseEntity.ok("Reservation created successfully with ID: " + reservationId);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create reservation: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + e.getMessage());
        }
    }


    @PutMapping("/spaces/release/{vehicleNo}")
    public ResponseEntity<?> releaseVehicleByVehicleNo(@PathVariable String vehicleNo) {
        try {
            String reservationId = reservationService.getActiveReservationByVehicleNo(vehicleNo);

            String spaceId = reservationService.endReservation(reservationId);

            parkingSpaceService.releaseSpace(spaceId);

            return ResponseEntity.ok("Vehicle released successfully from space ID: " + spaceId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Release failed: " + e.getMessage());
        }
    }

    @GetMapping("/reservations/active/{vehicleNo}")
    public ResponseEntity<?> getActiveReservationByVehicleNo(@PathVariable String vehicleNo) {
        System.out.println(vehicleNo);
        ParkingReservationDTO dto = reservationService.getActiveReservationDTOByVehicleNo(vehicleNo);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/reservations/getAll")
    public ResponseEntity<List<ParkingReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }
}*/

package lk.ijse.parkingspaceservice.controller;

import lk.ijse.parkingspaceservice.dto.ParkingReservationDTO;
import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingspaceservice.service.ParkingReservationService;
import lk.ijse.parkingspaceservice.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private ParkingReservationService reservationService;

    @PostMapping("/spaces/save")
    public ResponseEntity<String> saveParkingSpace(@RequestBody ParkingSpaceDTO dto) {
        return ResponseEntity.ok(parkingSpaceService.save(dto));
    }

    @GetMapping("/spaces/getAll")
    public ResponseEntity<List<ParkingSpaceDTO>> getAllSpaces() {
        return ResponseEntity.ok(parkingSpaceService.getAll());
    }

    @GetMapping("/spaces/available")
    public ResponseEntity<List<ParkingSpaceDTO>> getAvailableSpaces() {
        return ResponseEntity.ok(parkingSpaceService.getAvailableSpaces());
    }

    @PutMapping("/spaces/reserve/{vehicleNo}")
    public ResponseEntity<?> reserveSpaceAndCreateReservation(@PathVariable String vehicleNo) {
        try {
            String spaceId = parkingSpaceService.reserveSpace(vehicleNo);
            String reservationId = reservationService.createReservation(vehicleNo, spaceId);
            return ResponseEntity.ok("Reservation created successfully with ID: " + reservationId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create reservation: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + e.getMessage());
        }
    }

    @PutMapping("/spaces/release/{vehicleNo}")
    public ResponseEntity<?> releaseVehicleByVehicleNo(@PathVariable String vehicleNo) {
        try {
            System.out.println("Attempting to release vehicle: " + vehicleNo);
            String reservationId = reservationService.getActiveReservationByVehicleNo(vehicleNo);
            System.out.println("Found reservation ID: " + reservationId);
            String spaceId = reservationService.endReservation(reservationId);
            System.out.println("Ended reservation, releasing space: " + spaceId);
            parkingSpaceService.releaseSpace(spaceId);
            return ResponseEntity.ok("Vehicle released successfully from space ID: " + spaceId);
        } catch (RuntimeException e) {
            System.out.println("Release failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Release failed: " + e.getMessage());
        }
    }

    @GetMapping("/reservations/active/{vehicleNo}")
    public ResponseEntity<?> getActiveReservationByVehicleNo(@PathVariable String vehicleNo) {
        System.out.println(vehicleNo);
        ParkingReservationDTO dto = reservationService.getActiveReservationDTOByVehicleNo(vehicleNo);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/reservations/getAll")
    public ResponseEntity<List<ParkingReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }
}