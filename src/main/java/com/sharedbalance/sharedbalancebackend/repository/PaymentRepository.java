package com.sharedbalance.sharedbalancebackend.repository;

import com.sharedbalance.sharedbalancebackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
     List<Payment> findByGroupId(Long groupId);
     List<Payment> findByGroupIdAndPaidFalse(Long groupId);
}
