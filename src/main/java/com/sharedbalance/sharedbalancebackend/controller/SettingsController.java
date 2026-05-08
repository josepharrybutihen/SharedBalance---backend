package com.sharedbalance.sharedbalancebackend.controller;

import com.sharedbalance.sharedbalancebackend.entity.UserSettings;
import com.sharedbalance.sharedbalancebackend.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:3000",
    "https://shared-balance-azure.vercel.app"
})
public class SettingsController {

    private final UserSettingsRepository userSettingsRepository;

    @GetMapping("/settings")
    public Map<String,Object> getSettings(){

        UserSettings settings;

        if(userSettingsRepository.count() == 0){

            settings = new UserSettings();
            settings.setPushNotifications(false);
            settings.setLanguage("English");

            userSettingsRepository.save(settings);

        }else{

            settings = userSettingsRepository.findAll().get(0);
        }

        return Map.of(
                "payload", settings
        );
    }

    @PutMapping("/settings")
    public Map<String,Object> updateSettings(@RequestBody UserSettings req){

        UserSettings settings = userSettingsRepository.findAll().get(0);

        settings.setPushNotifications(req.isPushNotifications());
        settings.setLanguage(req.getLanguage());

        userSettingsRepository.save(settings);

        return Map.of(
                "message","Settings updated"
        );
    }
}
