package dev.jaoow.pixpaymentservice.producer;

import dev.jaoow.pixpaymentservice.config.RabbitMQConfig;
import dev.jaoow.pixpaymentservice.entity.PixPayment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentConfirmationProducer {

    private final RabbitTemplate rabbitTemplate;

    public PaymentConfirmationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentConfirmedMessage(PixPayment payment) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.PAYMENT_CONFIRMED_QUEUE,
                payment
        );
    }
}
