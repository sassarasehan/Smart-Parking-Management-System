package lk.ijse.paymentservice.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lk.ijse.parkingspaceservice.dto.ParkingReservationDTO;
import lk.ijse.paymentservice.dto.MockCardDTO;
import lk.ijse.paymentservice.entity.Payment;
import lk.ijse.paymentservice.repo.PaymentRepo;
import lk.ijse.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentRepo paymentRepo;

    @Override
    public String processPaymentByLicensePlate(String licensePlate, MockCardDTO cardDTO) {
        if (!isValidCard(cardDTO)) {
            throw new RuntimeException("Invalid card details.");
        }

        String fetchUrl = "http://localhost:8083/parking-service/api/v1/parking/reservations/active/" + licensePlate;
        ResponseEntity<ParkingReservationDTO> res = restTemplate.getForEntity(fetchUrl, ParkingReservationDTO.class);

        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
            throw new RuntimeException("No active reservation found for vehicle: " + licensePlate);
        }

        ParkingReservationDTO reservation = res.getBody();

        String releaseUrl = "http://localhost:8083/parking-service/api/v1/parking/spaces/release/" + licensePlate;
        restTemplate.put(releaseUrl, null); // no request body needed

        long hours = Math.max(Duration.between(reservation.getStartTime(), LocalDateTime.now()).toHours(), 1);
        double fee = hours * 200.0;

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .licensePlate(licensePlate)
                .reservationId(reservation.getReservationId())
                .amount(fee)
                .status("SUCCESS")
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepo.save(payment);

        generateReceiptPDF(payment, reservation, cardDTO);

        return "Payment successful. Rs. " + fee;
    }

    private boolean isValidCard(MockCardDTO dto) {
        return dto.getCardNumber().startsWith("4") && dto.getCvv().length() == 3;
    }

    private void generateReceiptPDF(Payment payment, ParkingReservationDTO reservation, MockCardDTO cardDTO) {
        Document document = new Document();
        try {
            String folderPath = "receipts";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = folderPath + "/Receipt_" + payment.getPaymentId() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            document.open();

            Paragraph title = new Paragraph("Smart Parking Receipt", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ", textFont));

            document.add(new Paragraph(new String(new char[150]).replace("\0", "-"), textFont));
            document.add(new Paragraph(" ", textFont));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            addTableCell(table, "Payment ID:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, payment.getPaymentId(), textFont, Element.ALIGN_LEFT);

            addTableCell(table, "Vehicle No:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, payment.getLicensePlate(), textFont, Element.ALIGN_LEFT);

            addTableCell(table, "Reservation ID:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, payment.getReservationId(), textFont, Element.ALIGN_LEFT);

            addTableCell(table, "Start Time:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, reservation.getStartTime().toString(), textFont, Element.ALIGN_LEFT);

            addTableCell(table, "End Time:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, reservation.getEndTime() != null ? reservation.getEndTime().toString() : "N/A", textFont, Element.ALIGN_LEFT);

            addTableCell(table, "Total Paid:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, "Rs. " + payment.getAmount(), textFont, Element.ALIGN_LEFT);

            addTableCell(table, "Status:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, payment.getStatus(), textFont, Element.ALIGN_LEFT);

            addTableCell(table, "Paid At:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, payment.getPaidAt().toString(), textFont, Element.ALIGN_LEFT);

            addTableCell(table, "Payment Method:", headerFont, Element.ALIGN_LEFT);
            addTableCell(table, "Card ending in " + cardDTO.getCardNumber().substring(cardDTO.getCardNumber().length() - 4), textFont, Element.ALIGN_LEFT);

            document.add(table);

            document.add(new Paragraph(" ", textFont));
            document.add(new Paragraph(new String(new char[50]).replace("\0", "="), textFont));
            Paragraph footer = new Paragraph("Thank You for Choosing Smart Parking! \u2728", textFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            document.add(new Paragraph("Contact us: support@smartparking.com | Visit: www.smartparking.com", textFont));
            document.add(new Paragraph(new String(new char[50]).replace("\0", "="), textFont));

            document.close();
            System.out.println("PDF receipt generated: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to generate PDF: " + e.getMessage());
        }
    }

    private void addTableCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
    }
}