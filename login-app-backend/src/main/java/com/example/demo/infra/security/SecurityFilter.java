package com.example.demo.infra.security;


import com.example.demo.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter { // filtro que vai executar uma vez para cada requisição

    // É uma configuração de segurança que permite que o servidor web restrinja o acesso a um recurso,
    // dando permissão apenas a usuários autorizados.
    // É uma implementação padrão

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        if (login != null) { // se o login for diferente de nulo, ele vai buscar o usuário no banco de dados
        var user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User not found")); // se não encontrar o usuário, ele vai lançar uma exceção

            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // cria uma lista de autorizações
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities); // cria um token de autenticação passando o usuário e as autorizações

            SecurityContextHolder.getContext().setAuthentication(authentication); // Montado o objeto, joga no contexto do Spring Security
            // É o que vai permitir que o usuário acesse a aplicação
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        //Recebe o quest do usuario, onde está o token de autenticação(Authorization)
        // se não tiver nada retorna null
        // se tiver algo, retorna o token substituindo o "Bearer " por nada, para pegar somente o token
        // se mudar onde o token está vindo, mudar aqui também
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
        }
    }


