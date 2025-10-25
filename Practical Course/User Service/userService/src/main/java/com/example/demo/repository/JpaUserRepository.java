package com.example.demo.repository;

import com.example.demo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    User save(User user);

    @Query("SELECT u FROM User u JOIN FETCH u.cards WHERE u.id = :id")
    Optional<User> getUserById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"cards"})
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"cards"})
    Optional<User> findUserByEmail(String email);

    void deleteById(Long id);
}
