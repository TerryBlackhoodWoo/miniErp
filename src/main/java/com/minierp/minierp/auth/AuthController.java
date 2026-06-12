package com.minierp.minierp.auth;

import com.minierp.minierp.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return userRepo.findByUsername(req.username())
                .filter(u -> u.isActive() && encoder.matches(req.password(), u.getPassword()))
                .map(u -> ResponseEntity.ok(Map.of(
                        "token", jwtUtil.generate(u.getUsername(), u.getRole()),
                        "name",  u.getName(),
                        "role",  u.getRole()
                )))
                .orElse(ResponseEntity.status(401).build());
    }

    public record LoginRequest(String username, String password) {}
}