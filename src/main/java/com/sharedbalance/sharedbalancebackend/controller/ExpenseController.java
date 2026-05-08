package com.sharedbalance.sharedbalancebackend.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharedbalance.sharedbalancebackend.entity.Expenses;
import com.sharedbalance.sharedbalancebackend.entity.Transaction;
import com.sharedbalance.sharedbalancebackend.repository.ExpenseRepository;
import com.sharedbalance.sharedbalancebackend.repository.GroupRepository;
import com.sharedbalance.sharedbalancebackend.repository.TransactionRepository;
import com.sharedbalance.sharedbalancebackend.entity.Payment;
import com.sharedbalance.sharedbalancebackend.repository.PaymentRepository;
import com.sharedbalance.sharedbalancebackend.entity.Group;
import com.sharedbalance.sharedbalancebackend.services.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final TransactionRepository transactionRepository;
    private final GroupRepository groupRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    @PostMapping("/calculate")
    public Map<String, Object> calculate(@RequestBody Map<String, Object> request) {

    Long groupId = Long.parseLong(request.get("groupId").toString());

    // ✅ BLOCK second calculation
    if (expenseRepository.existsByGroupId(groupId)) {
        throw new RuntimeException("Expenses already calculated for this group");
    }

    List<String> participantsList = (List<String>) request.get("participants");
    String payer = request.get("payer").toString();
    double amount = Double.parseDouble(request.get("amount").toString());

    String groupName = request.get("groupName") != null
            ? request.get("groupName").toString()
            : "Unknown Group";

    groupId = request.get("groupId") != null
            ? Long.parseLong(request.get("groupId").toString())
            : null;

    int participants = participantsList.size();

    double split = amount / participants;

    Map<String, String> breakdown = new LinkedHashMap<>();

    for (String person : participantsList) {
        if (person.equals(payer)) {
            double value = amount - split;
            breakdown.put(person, "+" + String.format("%.2f", value));
        } else {
            breakdown.put(person, "-" + String.format("%.2f", split));
        }
    }

    // ✅ CREATE PAYMENT RECORDS
    for (String person : participantsList) {
        if (!person.equals(payer)) {

            Payment payment = new Payment();
            payment.setGroupId(groupId);
            payment.setPayer(person);      // who owes
            payment.setReceiver(payer);    // who gets paid
            payment.setAmount(split);
            payment.setPaid(false);

            paymentRepository.save(payment);
        }
        System.out.println("Creating payment:");
        System.out.println("groupId=" + groupId);
        System.out.println("payer=" + person);
        System.out.println("receiver=" + payer);
}

    try {
        ObjectMapper mapper = new ObjectMapper();

        // ✅ Convert breakdown → JSON string
        String breakdownJson = mapper.writeValueAsString(breakdown);

        Expenses expense = new Expenses();
        expense.setPayer(payer);
        expense.setTotalAmount(amount);
        expense.setParticipantCount(participants);
        expense.setParticipants(String.join(",", participantsList));

        // ✅ SAVE THESE
        expense.setBreakdown(breakdownJson);
        expense.setGroupId(groupId);
        expense.setGroupName(groupName);

        expenseRepository.save(expense);

        for (String member : participantsList) {

    if (!member.equals(payer)) {

        notificationService.send(
            member,
            payer,
            "EXPENSE_ADDED",
            "🧾 " + payer + " added ₱" + amount + " expense in " + groupName
        );
    }
}

    } catch (Exception e) {
        throw new RuntimeException("Error saving breakdown");
    }

    // transaction (unchanged)
    Transaction t = new Transaction();
    t.setGroupId(groupId);
    t.setGroupName(groupName);
    t.setName(payer);
    t.setTransactionType("Expense Added: ₱" + amount);
    t.setDate(LocalDate.now());

    transactionRepository.save(t);

    return Map.of(
            "payload", breakdown,
            "splitPerPerson", split,
            "groupName", groupName,
            "totalAmount", amount
    );
    }

    @GetMapping("/group/{groupId}")
    public List<Expenses> getByGroup(
            @PathVariable Long groupId,
            @RequestParam String email
    ) {

        // ✅ Get the group first
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // ✅ Check if user is allowed
        boolean isCreator = group.getCreatorEmail().equals(email);
        boolean isMember = group.getMembers() != null && group.getMembers().contains(email);

        if (!isCreator && !isMember) {
            throw new RuntimeException("Unauthorized access to this group");
        }

        // ✅ If allowed, return expenses
        return expenseRepository.findByGroupId(groupId);
    }
}