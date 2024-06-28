package dev.jaoow.pixpaymentservice.service;

import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import dev.jaoow.pixpaymentservice.entity.PixPayment;
import dev.jaoow.pixpaymentservice.model.PixPaymentRequest;
import dev.jaoow.pixpaymentservice.model.PixPaymentResponse;
import dev.jaoow.pixpaymentservice.repository.PixPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PixPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PixPaymentService.class);

    private final PixPaymentRepository pixPaymentRepository;

    @Value("${server.url}")
    private String serverUrl;

    public PixPaymentService(PixPaymentRepository pixPaymentRepository) {
        this.pixPaymentRepository = pixPaymentRepository;
    }

    public PixPaymentResponse createPixPayment(PixPaymentRequest request) {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("x-idempotency-key", UUID.randomUUID().toString());

        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .customHeaders(customHeaders)
                .build();

        PaymentClient client = new PaymentClient();

        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(BigDecimal.valueOf(request.getAmount()))
                .description(request.getDescription())
                .notificationUrl(serverUrl + "/api/ipn")
                .paymentMethodId("pix")
                .dateOfExpiration(OffsetDateTime.now().plusDays(1)) // Validade do pagamento
                .payer(PaymentPayerRequest.builder()
                        .email(request.getEmail())
                        .firstName(request.getPayerFirstName())
                        .identification(IdentificationRequest.builder()
                                .type("CPF")
                                .number(request.getPayerDocumentNumber())
                                .build())
                        .build())
                .build();

        // Criar o pagamento usando o MercadoPago SDK
        Payment payment;
        try {
            payment = client.create(paymentCreateRequest, requestOptions);
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException e) {
            logger.error("Error while creating payment: {}", e.getApiResponse().getContent());
            throw new RuntimeException(e);
        }

        // Persistir o pagamento no banco de dados
        PixPayment pixPayment = new PixPayment();
        pixPayment.setPaymentId(payment.getId());
        pixPayment.setAmount(request.getAmount());
        pixPayment.setEmail(request.getEmail());
        pixPayment.setStatus(payment.getStatus());
        pixPayment.setPayerFirstName(request.getPayerFirstName());
        pixPayment.setPayerDocumentNumber(request.getPayerDocumentNumber());

        pixPaymentRepository.save(pixPayment);

        return buildPixPaymentResponse(payment);
    }

    private PixPaymentResponse buildPixPaymentResponse(Payment payment) {
        PixPaymentResponse response = new PixPaymentResponse();
        response.setPaymentId(payment.getId().toString());
        response.setStatus(payment.getStatus());
        response.setQrCode(payment.getPointOfInteraction().getTransactionData().getQrCode());
        response.setQrCodeBase64(payment.getPointOfInteraction().getTransactionData().getQrCodeBase64());
        response.setTicketUrl(payment.getPointOfInteraction().getTransactionData().getTicketUrl());
        return response;
    }
}
