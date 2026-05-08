package com.sharedbalance.sharedbalancebackend.repository;

import com.sharedbalance.sharedbalancebackend.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expenses, Long> {
List<Expenses> findByGroupId(Long groupId);
boolean existsByGroupId(Long groupId);
}