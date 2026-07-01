package com.minierp.minierp.auth;

import com.minierp.minierp.entity.AppUser;
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

    /* ── 로그인 ─────────────────────────────────────── */
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

    /* ── 회원가입 ────────────────────────────────────── */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        // 아이디 중복 검사
        if (userRepo.findByUsername(req.username()).isPresent()) {
            return ResponseEntity.status(409)
                    .body(Map.of("message", "이미 사용 중인 아이디입니다."));
        }

        // 최소 길이 검증 (서버 사이드)
        if (req.username() == null || req.username().length() < 3) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "아이디는 3자 이상이어야 합니다."));
        }
        if (req.password() == null || req.password().length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "비밀번호는 6자 이상이어야 합니다."));
        }

        AppUser user = new AppUser(
                req.username(),
                encoder.encode(req.password()),
                req.name(),
                "USER"   // 기본 role
        );
        userRepo.save(user);

        return ResponseEntity.status(201)
                .body(Map.of("message", "가입이 완료되었습니다."));
    }

    /* ── DTO ─────────────────────────────────────────── */
    public record LoginRequest(String username, String password) {}
    public record RegisterRequest(String username, String password, String name) {}
}