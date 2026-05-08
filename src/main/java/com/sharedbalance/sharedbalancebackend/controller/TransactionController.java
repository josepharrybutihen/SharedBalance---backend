package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.repository.GroupRepository;
import com.sharedbalance.sharedbalancebackend.repository.TransactionRepository;
import com.sharedbalance.sharedbalancebackend.entity.Transaction;
import lombok.RequiredArgsConstructor;
import com.sharedbalance.sharedbalancebackend.entity.Group;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://shared-balance-azure.vercel.app"
})
public class TransactionController {

        private final GroupRepository groupRepository;
        private final TransactionRepository transactionRepository;


        @GetMapping
    public List<Transaction> getUserTransactions(@RequestParam String email) {
           return transactionRepository
            .findByNameOrderByDateDesc(email)
            .stream()
            .filter(t -> 
                t.getTransactionType().toLowerCase().contains("paid") ||
                t.getTransactionType().toLowerCase().contains("received")
            )
            .toList();
       

}
}
