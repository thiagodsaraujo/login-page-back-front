package com.example.demo.controllers;


import com.example.demo.domain.user.User;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.infra.security.TokenService;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // faça os métodos necessários

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // faça os métodos necessários

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {

        User user = userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        // tratamento de exceção para usuário não encontrado, controller advice!

        // caso as senhas deem match
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {

        Optional<User> user = userRepository.findByEmail(body.email()); // verificando se o usuário já existe

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));

            newUser.setName(body.name());
            newUser.setEmail(body.email());
            this.userRepository.save(newUser);

            String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));

        }

        return ResponseEntity.badRequest().build();
    }
}
