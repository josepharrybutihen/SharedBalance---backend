package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.entity.Group;
import com.sharedbalance.sharedbalancebackend.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:3000")
public class GroupController {

    private final GroupRepository groupRepository;

    @PostMapping("/create")
    public Map<String,Object> createGroup(@RequestBody Group group){

        groupRepository.save(group);

        return Map.of(
                "message","Group created"
        );
    }

    @GetMapping
    public Map<String,Object> getGroups(){

        List<Group> groups = groupRepository.findAll();

        return Map.of(
                "payload",groups
        );
    }

}
