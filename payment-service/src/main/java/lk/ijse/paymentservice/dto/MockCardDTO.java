package lk.ijse.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockCardDTO {
    private String cardNumber;
    private String cardHolder;
    private String expiry;
    private String cvv;
}