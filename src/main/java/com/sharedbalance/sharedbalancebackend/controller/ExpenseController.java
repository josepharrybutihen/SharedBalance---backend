package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.entity.Expenses;
import com.sharedbalance.sharedbalancebackend.entity.Transaction;
import com.sharedbalance.sharedbalancebackend.repository.ExpenseRepository;
import com.sharedbalance.sharedbalancebackend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final TransactionRepository transactionRepository;

    @PostMapping("/calculate")
    public Map<String, Object> calculate(@RequestBody Map<String, Object> request) {

        int participants = ((java.util.List<?>) request.get("participants")).size();
        double amount = Double.parseDouble(request.get("amount").toString());
        String payer = request.get("payer").toString();

        double split = amount / participants;

        Expenses expense = new Expenses();
        expense.setPayer(payer);
        expense.setTotalAmount(amount);
        expense.setParticipantCount(participants);

        expenseRepository.save(expense);

        // Save transaction history
        Transaction t = new Transaction();
        t.setGroupName("Sample Group");
        t.setName(payer);
        t.setTransactionType("Expense Added");
        t.setDate(LocalDate.now());

        transactionRepository.save(t);

        return Map.of(
                "splitPerPerson", split
        );
    }
}