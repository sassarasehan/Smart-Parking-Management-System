package lk.ijse.paymentservice.service;

import lk.ijse.paymentservice.dto.MockCardDTO;

public interface PaymentService {
    String processPaymentByLicensePlate(String licensePlate, MockCardDTO cardDTO);
}