package dev.jaoow.pixpaymentservice.controller;

import dev.jaoow.pixpaymentservice.service.PaymentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ipn")
public class PaymentNotificationController {

   private final PaymentNotificationService notificationService;

    public PaymentNotificationController(PaymentNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public void handleIPN(@RequestParam("topic") String topic, @RequestParam("id") String id) {
        if (topic.equals("payment")) {
           notificationService.handlePaymentNotification(id);
        }
    }
}
