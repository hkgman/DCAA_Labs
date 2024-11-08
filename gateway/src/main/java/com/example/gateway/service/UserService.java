package com.example.gateway.service;

import com.example.gateway.model.Role;
import com.example.gateway.model.User;
import com.example.gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User create(String email, String password, Role role) {
        final User user = new User(null, email, password, role);
        return userRepository.save(user);
    }
    public Optional<User> find(String email) {
        return userRepository.findByEmail(email);
    }
}
