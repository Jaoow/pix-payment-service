package dev.jaoow.pixpaymentservice.repository;

import dev.jaoow.pixpaymentservice.entity.PixPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PixPaymentRepository extends JpaRepository<PixPayment, Long> {
    PixPayment findByPaymentId(Long paymentId);
}
