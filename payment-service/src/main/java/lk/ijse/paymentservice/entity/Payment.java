package lk.ijse.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    private String paymentId;

    private String licensePlate;
    private String reservationId;
    private double amount;
    private String status;
    private LocalDateTime paidAt;
}