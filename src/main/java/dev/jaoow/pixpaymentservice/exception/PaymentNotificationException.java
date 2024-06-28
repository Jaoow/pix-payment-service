package dev.jaoow.pixpaymentservice.exception;

public class PaymentNotificationException extends RuntimeException {

    public PaymentNotificationException(String message) {
        super(message);
    }

    public PaymentNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
