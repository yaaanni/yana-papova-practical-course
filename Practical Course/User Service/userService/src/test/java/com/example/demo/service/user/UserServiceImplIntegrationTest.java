package com.example.demo.service.user;

import com.example.demo.dto.CardDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapping.CardMapping;
import com.example.demo.dto.mapping.UserMapping;
import com.example.demo.entities.User;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import com.example.demo.service.User.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Testcontainers
@Transactional
public class UserServiceImplIntegrationTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JpaCardRepository jpaCardRepository;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private UserMapping userMapping;
    @Autowired
    private CardMapping cardMapping;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.0")
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());


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
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @BeforeEach
    void clearCache() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void shouldCreateUserWithoutCards() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto savedUser = userService.createUser(userDto);
        UserDto userFromService = userService.getUserById(savedUser.getId());
        assertNotNull(userFromService.getId());
        assertTrue(userFromService.getCards().isEmpty());
        Optional<User> userFromDb = jpaUserRepository.getUserById(savedUser.getId());
        assertTrue(userFromDb.isPresent());
        assertTrue(userFromDb.get().getCards().isEmpty());
    }

    @Test
    void shouldCreateUserWithCards() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>(Arrays.asList(
                        new CardDto(
                                null,
                                null,
                                1234123412341234L,
                                "Anastasia Krivchik",
                                LocalDate.of(2026, 9, 6)
                        ),
                        new CardDto(
                                null,
                                null,
                                5555666677778888L,
                                "Anastasia Krivchik",
                                LocalDate.of(2027, 5, 15)
                        )
                )));

        UserDto savedUser = userService.createUser(userDto);
        UserDto userFromService = userService.getUserById(savedUser.getId());
        assertNotNull(userFromService.getId());
        assertFalse(userFromService.getCards().isEmpty());
        Optional<User> userFromDb = jpaUserRepository.getUserById(savedUser.getId());
        assertTrue(userFromDb.isPresent());
        assertFalse(userFromDb.get().getCards().isEmpty());
    }

}
