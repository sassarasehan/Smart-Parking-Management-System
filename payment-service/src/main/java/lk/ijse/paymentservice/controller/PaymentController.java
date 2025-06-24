package lk.ijse.paymentservice.controller;

import lk.ijse.paymentservice.dto.MockCardDTO;
import lk.ijse.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<String> handleVehiclePayment(
            @RequestParam String licensePlate,
            @RequestBody MockCardDTO cardDTO
    ) {
        try {
            String message = paymentService.processPaymentByLicensePlate(licensePlate, cardDTO);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Payment failed: " + e.getMessage());
        }
    }
}