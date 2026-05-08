package com.sharedbalance.sharedbalancebackend.repository;

import com.sharedbalance.sharedbalancebackend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Long> {
    // ✅ Get groups where user is creator OR member
    List<Group> findByCreatorEmailOrMembersContaining(String creatorEmail, String memberEmail);
}
