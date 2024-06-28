package dev.jaoow.pixpaymentservice.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import dev.jaoow.pixpaymentservice.entity.PixPayment;
import dev.jaoow.pixpaymentservice.exception.PaymentNotificationException;
import dev.jaoow.pixpaymentservice.producer.PaymentConfirmationProducer;
import dev.jaoow.pixpaymentservice.repository.PixPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentNotificationService.class);

    private final PixPaymentRepository pixPaymentRepository;
    private final PaymentConfirmationProducer paymentConfirmationProducer;

    public PaymentNotificationService(PixPaymentRepository pixPaymentRepository, PaymentConfirmationProducer paymentConfirmationProducer) {
        this.pixPaymentRepository = pixPaymentRepository;
        this.paymentConfirmationProducer = paymentConfirmationProducer;
    }

    public void handlePaymentNotification(String paymentId) {
        try {
            Long id = Long.valueOf(paymentId);
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(id);

            PixPayment pixPayment = pixPaymentRepository.findByPaymentId(id);
            if (pixPayment != null && payment != null) {
                pixPayment.setStatus(payment.getStatus());
                pixPaymentRepository.save(pixPayment);

                logger.info("Payment updated ID: {}, Status: {}", paymentId, payment.getStatus());
                paymentConfirmationProducer.sendPaymentConfirmedMessage(pixPayment);
            } else {
                throw new PaymentNotificationException("Payment not found with ID: " + paymentId);
            }
        } catch (MPApiException e) {
            logger.error("Error while handling IPN: {}", e.getApiResponse().getContent());
            throw new PaymentNotificationException("Error while handling IPN", e);
        } catch (MPException e) {
            throw new PaymentNotificationException("Unexpected error while processing payment", e);
        }
    }
}
