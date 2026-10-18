package com.wangyu.repository;

import java.util.Optional;

import com.wangyu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    <T> Optional<T> getUserById(Long id, Class<T> type);

    <T> Optional<T> getUserByEmail(String email, Class<T> type);

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationCode(String activationCode);

    boolean existsByEmail(String email);
}
