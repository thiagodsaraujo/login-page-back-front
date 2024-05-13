package com.example.demo.infra.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // classe de configuração
@EnableWebSecurity // habilitando a segurança
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService; // injetando a classe de serviço

    @Autowired
    SecurityFilter securityFilter; // injetando o filtro de segurança

    private final String[] PUBLIC_MATCHERS = {
            "/h2-console/**",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    // Configuração de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desabilitando o csrf pois não estamos usando sessão
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // desabilitando a sessão, padrão das apis restfull
                // o token é enviado em todas as requisições
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // permitindo o login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // permitindo o registro
                        .anyRequest().authenticated() // qualquer outra requisição precisa de autenticação
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
                // adicionando o filtro de segurança(SecurityFilter) antes do filtro de autenticação padrão

        return http.build(); // construindo a configuração
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // retornando o encoder customizado, vai usar no controller
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
        // Bean necessário para o Spring Security funcionar corretamente
    }


}
