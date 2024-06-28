package dev.jaoow.pixpaymentservice.controller;

import dev.jaoow.pixpaymentservice.model.PixPaymentRequest;
import dev.jaoow.pixpaymentservice.model.PixPaymentResponse;
import dev.jaoow.pixpaymentservice.service.PixPaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pix")
public class CreatePaymentController {

    private final PixPaymentService pixPaymentService;

    public CreatePaymentController(PixPaymentService pixPaymentService) {
        this.pixPaymentService = pixPaymentService;
    }

    @PostMapping("/create")
    public PixPaymentResponse createPixPayment(@RequestBody PixPaymentRequest request) {
        return pixPaymentService.createPixPayment(request);
    }
}
