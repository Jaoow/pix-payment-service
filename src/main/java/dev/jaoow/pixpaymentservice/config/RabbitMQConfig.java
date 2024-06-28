package dev.jaoow.pixpaymentservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_CONFIRMED_QUEUE = "payment-confirmed";
    public static final String EXCHANGE_NAME = "payments-exchange";

    @Bean
    Queue paymentConfirmedQueue() {
        return new Queue(PAYMENT_CONFIRMED_QUEUE, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding paymentConfirmedBinding(Queue paymentConfirmedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentConfirmedQueue).to(exchange).with(PAYMENT_CONFIRMED_QUEUE);
    }
}
