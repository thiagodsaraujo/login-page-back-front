package com.example.demo.repositories;


import com.example.demo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // faça os métodos necessários
    Optional<User> findByEmail(String email); // método para buscar o usuário pelo username, pode ser que não exista

}
