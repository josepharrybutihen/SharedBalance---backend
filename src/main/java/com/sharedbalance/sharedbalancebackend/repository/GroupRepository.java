package com.sharedbalance.sharedbalancebackend.repository;

import com.sharedbalance.sharedbalancebackend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group,Long> {
}
