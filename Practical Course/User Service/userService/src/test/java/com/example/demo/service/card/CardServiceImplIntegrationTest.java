package com.example.demo.service.card;

import com.example.demo.dto.CardDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapping.CardMapping;
import com.example.demo.entities.Card;
import com.example.demo.exception.CardAlreadyExist;
import com.example.demo.exception.CardNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import com.example.demo.service.Card.CardServiceImpl;
import com.example.demo.service.User.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;


@SpringBootTest
@Testcontainers
@Transactional
public class CardServiceImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private JpaCardRepository cardRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CardServiceImpl cardService;

    @Autowired
    private JpaCardRepository jpaCardRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private CardMapping cardMapping;

    @Test
    void shouldCreateCard() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        CardDto cardDto = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCard = cardService.createCard(cardDto);
        Optional<Card> cardFromDb = cardRepository.getCardById(savedCard.getId());
        assertTrue(cardFromDb.isPresent());
        assertEquals(savedUser.getId(), cardFromDb.get().getUser().getId());
    }

    @Test
    void shouldThrowExceptionWhenCardAlreadyExist() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        CardDto cardDto = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCard = cardService.createCard(cardDto);
        CardDto cardDtoException = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        assertThrows(CardAlreadyExist.class, () -> cardService.createCard(cardDtoException));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        CardDto cardDto = new CardDto(
                null,
                1000L,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        assertThrows(UserNotFoundException.class, () -> cardService.createCard(cardDto));
    }

    @Test
    void shouldGetCardById() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        CardDto cardDto = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCard = cardService.createCard(cardDto);
        Long id = savedCard.getId();
        assertEquals(savedCard, cardService.getCardById(id));
    }

    @Test
    void shouldThrowExceptionWhenCardNotFound() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        CardDto cardDto = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCard = cardService.createCard(cardDto);
        Long id = savedCard.getId() + 1;
        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(id));
    }

    @Test
    void shouldGetAllCards() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        cardService.createCard(new CardDto(
                null,
                savedUser.getId(),
                1111222233334444L,
                "Card 1",
                LocalDate.of(2026, 10, 3)));
        cardService.createCard(new CardDto(
                null, savedUser.getId(),
                5555666677778888L, "Card 2",
                LocalDate.of(2027, 5, 15)));
        Pageable pageable = PageRequest.of(0, 1);
        Page<CardDto> page = cardService.getAllCards(pageable);
        assertEquals(1, page.getContent().size());
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void shouldDeleteCardById() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        CardDto cardDto = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCardDto = cardService.createCard(cardDto);
        Long id = savedCardDto.getId();
        cardService.deleteCardById(id);
        assertFalse(cardRepository.getCardById(id).isPresent());
    }

    @Test
    void shouldThrowExceptionNotFoundCardWhenDelete() {
        assertThrows(CardNotFoundException.class, () -> cardService.deleteCardById(100L));
    }

    @Test
    void shouldUpdateCard() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        CardDto cardDtoForUpdate = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCardDtoForUpdate = cardService.createCard(cardDtoForUpdate);
        CardDto cardDto = new CardDto(
                null,
                savedUser.getId(),
                1234123412341235L,
                "Milana Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCardDto = cardService.createCard(cardDto);
        CardDto updatedCard = cardService.updateCard(savedCardDtoForUpdate.getId(), savedCardDto);
        assertEquals("Milana Krivchik", updatedCard.getHolder());
    }

    @Test
    void shouldThrowExceptionNotFoundCardWhenUpdateCard() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        CardDto cardDto = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Milana Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCardDto = cardService.createCard(cardDto);
        assertThrows(CardNotFoundException.class, () -> cardService.updateCard(1000L, savedCardDto));
    }

    @Test
    void shouldThrowExceptionNotFoundUserWhenUpdateCard() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        CardDto cardDtoForUpdate = new CardDto(
                null,
                savedUser.getId(),
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        CardDto savedCardDtoForUpdate = cardService.createCard(cardDtoForUpdate);
        CardDto cardDto = new CardDto(
                null,
                100L,
                1234123412341234L,
                "Milana Krivchik",
                LocalDate.of(2026, 9, 6)
        );
        assertThrows(UserNotFoundException.class, () -> cardService.updateCard(savedCardDtoForUpdate.getId(), cardDto));
    }

}