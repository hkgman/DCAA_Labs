package com.example.gateway.controller;

import com.example.gateway.dto.AuthRequest;
import com.example.gateway.service.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthUtil authUtil;
    @PostMapping("/register")
    public ResponseEntity<String> reg(
            @RequestBody AuthRequest req
    ) {
        return ResponseEntity.ok(authUtil.signUp(req));
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody AuthRequest req
    ) {
        return ResponseEntity.ok(authUtil.signIn(req));
    }
}
