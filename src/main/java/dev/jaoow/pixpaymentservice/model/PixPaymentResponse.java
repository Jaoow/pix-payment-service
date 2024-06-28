package dev.jaoow.pixpaymentservice.model;

import lombok.Data;

@Data
public class PixPaymentResponse {
    private String paymentId;
    private String status;
    private String qrCode;
    private String qrCodeBase64;
    private String ticketUrl;
}
