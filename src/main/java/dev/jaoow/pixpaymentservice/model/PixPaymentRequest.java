package dev.jaoow.pixpaymentservice.model;

import lombok.Data;

@Data
public class PixPaymentRequest {
    private String description;
    private Double amount;
    private String email;
    private String payerFirstName;
    private String payerDocumentNumber;
}
