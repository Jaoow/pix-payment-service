package dev.jaoow.pixpaymentservice.controller;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import dev.jaoow.pixpaymentservice.entity.PixPayment;
import dev.jaoow.pixpaymentservice.producer.PaymentConfirmationProducer;
import dev.jaoow.pixpaymentservice.repository.PixPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ipn")
public class PaymentNotificationController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentNotificationController.class);

    private final PixPaymentRepository pixPaymentRepository;
    private final PaymentConfirmationProducer paymentConfirmationProducer;

    public PaymentNotificationController(PixPaymentRepository pixPaymentRepository, PaymentConfirmationProducer paymentConfirmationProducer) {
        this.pixPaymentRepository = pixPaymentRepository;
        this.paymentConfirmationProducer = paymentConfirmationProducer;
    }

    @PostMapping
    public void handleIPN(@RequestParam("topic") String topic, @RequestParam("id") String id) {

        logger.info("Received IPN - Topic: {}, ID: {}", topic, id);

        if (topic.equals("payment")) {
            handlePaymentNotification(id);
        }
    }

    private void handlePaymentNotification(String paymentId) {
        try {
            Long id = Long.valueOf(paymentId);
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(id);

            PixPayment pixPayment = pixPaymentRepository.findByPaymentId(id);
            if (pixPayment != null) {
                pixPayment.setStatus(payment.getStatus());
                pixPaymentRepository.save(pixPayment);

                logger.info("Payment updated ID: {}, Status: {}", paymentId, payment.getStatus());
                paymentConfirmationProducer.sendPaymentConfirmedMessage(pixPayment);
            }
        } catch (MPApiException e) {
            logger.error("Error while handling IPN: {}", e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new RuntimeException(e);
        }
    }
}
