package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.entity.Group;
import com.sharedbalance.sharedbalancebackend.repository.GroupRepository;
import com.sharedbalance.sharedbalancebackend.services.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {

    private final GroupRepository groupRepository;
    private final NotificationService notificationService;


    // Updated the code to save and return the savedGroup
    @PostMapping("/create")
        public Map<String, Object> createGroup(@RequestBody Group group) {

        Group savedGroup = groupRepository.save(group);

        // ✅ notify all members except creator
        for (String member : savedGroup.getMembers()) {
                if (!member.equals(savedGroup.getCreatorEmail())) {

                notificationService.send(
                        member,
                        savedGroup.getCreatorEmail(),
                        "GROUP_ADDED",
                        "👥 You were added to group: " + savedGroup.getName()
                );
                }
        }

        return Map.of(
                "message", "Group created",
                "payload", savedGroup
        );
        }

    @GetMapping
    public Map<String, Object> getGroups(@RequestParam String email) {

    List<Group> groups = groupRepository
            .findByCreatorEmailOrMembersContaining(email, email);

    return Map.of(
            "payload", groups
    );
}

    @GetMapping("/{id}")
public Map<String, Object> getGroupById(@PathVariable Long id) {

    Group group = groupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Group not found"));

    return Map.of(
            "payload", group
    );
    }
}

