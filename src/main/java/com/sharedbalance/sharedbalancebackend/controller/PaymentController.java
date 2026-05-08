package com.sharedbalance.sharedbalancebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sharedbalance.sharedbalancebackend.entity.Group;

import com.sharedbalance.sharedbalancebackend.entity.Payment;
import com.sharedbalance.sharedbalancebackend.repository.GroupRepository;
import com.sharedbalance.sharedbalancebackend.repository.PaymentRepository;
import com.sharedbalance.sharedbalancebackend.repository.TransactionRepository;
import com.sharedbalance.sharedbalancebackend.services.NotificationService;
import com.sharedbalance.sharedbalancebackend.entity.Transaction;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;
import com.sharedbalance.sharedbalancebackend.entity.Notification;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final GroupRepository groupRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    
   @PutMapping("/{id}/mark-paid")
    public ResponseEntity<?> markAsPaid(
            @PathVariable Long id,
            @RequestParam String userEmail
    ) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Group group = groupRepository.findById(payment.getGroupId())
                .orElseThrow();

        if (!group.getCreatorEmail().equals(userEmail)) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        // ✅ mark as paid
        payment.setPaid(true);
        paymentRepository.save(payment);

        // ✅ NOTIFICATIONS (ONLY ONCE EACH)

        // Receiver gets "you received money"
        notificationService.send(
            payment.getReceiver(),
            payment.getPayer(),
            "PAYMENT_RECEIVED",
            "💰 " + payment.getPayer() + " paid you ₱" + payment.getAmount()
        );

        // Payer gets "you paid"
        notificationService.send(
            payment.getPayer(),
            payment.getReceiver(),
            "PAYMENT_PAID",
            "💸 You paid ₱" + payment.getAmount() + " to " + payment.getReceiver()
        );

        // ✅ TRANSACTION LOGGING

        Transaction paid = new Transaction();
        paid.setGroupId(group.getId());
        paid.setGroupName(group.getName());
        paid.setName(payment.getPayer());
        paid.setTransactionType(
            "Paid ₱" + payment.getAmount() + " to " + payment.getReceiver()
        );
        paid.setDate(LocalDate.now());

        transactionRepository.save(paid);

        Transaction received = new Transaction();
        received.setGroupId(group.getId());
        received.setGroupName(group.getName());
        received.setName(payment.getReceiver());
        received.setTransactionType(
            "Received ₱" + payment.getAmount() + " from " + payment.getPayer()
        );
        received.setDate(LocalDate.now());

        transactionRepository.save(received);

        return ResponseEntity.ok("Marked as paid");
    }

    // ✅ ADD THIS HERE
    @GetMapping("/group/{groupId}")
    public List<Payment> getPayments(@PathVariable Long groupId) {
        return paymentRepository.findByGroupId(groupId);
    }

    @GetMapping("/balance")
public Map<String, Object> getUserBalance(@RequestParam String email) {

    List<Group> groups = groupRepository
            .findByCreatorEmailOrMembersContaining(email, email);

    double totalToPay = 0;
    double totalToReceive = 0;

    for (Group g : groups) {

        List<Payment> payments = paymentRepository.findByGroupId(g.getId());

        for (Payment p : payments) {
            if (!p.isPaid()) {

                if (p.getPayer().equals(email)) {
                    totalToPay += p.getAmount();
                }

                if (p.getReceiver().equals(email)) {
                    totalToReceive += p.getAmount();
                }
            }
        }
    }

    return Map.of(
            "toPay", totalToPay,
            "toReceive", totalToReceive,
            "netBalance", totalToReceive - totalToPay,
            "groups", groups
    );
}
}

