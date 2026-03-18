package com.sharedbalance.sharedbalancebackend.repository;

import com.sharedbalance.sharedbalancebackend.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
}
