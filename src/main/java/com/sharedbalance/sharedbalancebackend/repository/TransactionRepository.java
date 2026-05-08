package com.sharedbalance.sharedbalancebackend.repository;

import com.sharedbalance.sharedbalancebackend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
List<Transaction> findByGroupIdIn(List<Long> groupIds);
List<Transaction> findByNameOrderByDateDesc(String name);

}