package com.example.my_automation.service;


import com.example.my_automation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Long> findUserIdByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }
}