package com.example.demo.infra.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.demo.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}") // pegando a variavel do arquivo de propriedades
    private String secretAppProperties; // pegando a variavel do arquivo de propriedades

    // Método de criação do Token
    public String generateToken(User user) {

        // testes de get da variavel
        var secret = System.getenv("secret"); // pegando a variavel do sistema

        var secretVMOptions = System.getProperty("secret"); // pegando a variavel das configurações de execução

        if (secretVMOptions != null) {
            System.out.println("O valor da variável secretVMOptions é: " + secretVMOptions);
        } else {
            System.out.println("A variável secretVMOptions não está definida no sistema.");
        }

        if (secret != null) {
            System.out.println("O valor da variável SECRET_ENV é: " + secret);
        } else {
            System.out.println("A variável SECRET_ENV não está definida no sistema.");
        }

        try {

            Algorithm algorithm = Algorithm.HMAC256(secretAppProperties);

            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;

        } catch (JWTCreationException exception){
           throw new RuntimeException("Error while authenticating user.");
        }

    }

    public String validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretAppProperties);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            return null;
            // Vai retornar nulo e na cadeia de chamadas vai ser tratado com as verificações e o usuario não sera autenticado
        }

    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
