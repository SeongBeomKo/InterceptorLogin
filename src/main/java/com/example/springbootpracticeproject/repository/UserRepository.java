package com.example.springbootpracticeproject.repository;

import com.example.springbootpracticeproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //data 가 많아지면 indexing 필요
    Boolean existsByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
