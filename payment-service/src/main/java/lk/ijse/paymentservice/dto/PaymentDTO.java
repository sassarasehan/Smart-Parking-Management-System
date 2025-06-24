package lk.ijse.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private String licensePlate;
    private MockCardDTO card;
}