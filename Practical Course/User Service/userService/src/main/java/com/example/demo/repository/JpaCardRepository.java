package com.example.demo.repository;

import com.example.demo.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaCardRepository extends JpaRepository<Card, Long> {
    Card save(Card card);

    @Query(value = "SELECT * FROM card_info WHERE id = :id", nativeQuery = true)
    Optional<Card> getCardById(@Param("id") Long id);

    @Query("SELECT c FROM Card c")
    Page<Card> getAll(Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM card_info WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);

    boolean existsByNumber(Long number);
}
