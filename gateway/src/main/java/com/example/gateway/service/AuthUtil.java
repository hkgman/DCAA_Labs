package com.example.gateway.service;

import com.example.gateway.dto.AuthRequest;
import com.example.gateway.model.Role;
import com.example.gateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUtil {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public String signUp(AuthRequest req) {
        userService.create(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                Role.USER
        );
        return "Success";
    }
    public String signIn(AuthRequest req) {
        final User user = userService.find(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Incorrect email or password"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect email or password");
        }
        return jwtUtil.generateToken(user);
    }
}
