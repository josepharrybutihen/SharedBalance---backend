package com.sharedbalance.sharedbalancebackend.repository;

import com.sharedbalance.sharedbalancebackend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}