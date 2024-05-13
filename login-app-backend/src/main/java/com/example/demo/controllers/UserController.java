package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @GetMapping("/get") // Só para testar se a autenticação está funcionando após o login
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Sucesso!");
    }
}
