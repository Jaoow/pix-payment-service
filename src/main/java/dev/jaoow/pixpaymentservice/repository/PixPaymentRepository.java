package dev.jaoow.pixpaymentservice.repository;

import dev.jaoow.pixpaymentservice.entity.PixPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PixPaymentRepository extends JpaRepository<PixPayment, Long> {
    PixPayment findByPaymentId(Long paymentId);
}
