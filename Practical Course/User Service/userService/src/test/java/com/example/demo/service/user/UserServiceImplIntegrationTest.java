package com.example.demo.service.user;

import com.example.demo.dto.CardDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapping.CardMapping;
import com.example.demo.dto.mapping.UserMapping;
import com.example.demo.entities.User;
import com.example.demo.exception.UserAlreadyExists;
import com.example.demo.exception.UserEmailNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import com.example.demo.service.Card.CardServiceImpl;
import com.example.demo.service.User.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private CacheManager cacheManager;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private CardServiceImpl cardService;
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

    @Test
    void testUserIsCachedAfterCreation() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto created = userService.createUser(userDto);
        Cache cache = cacheManager.getCache("users");
        assertNotNull(cache);
        UserDto cached = cache.get(created.getId(), UserDto.class);
        assertNotNull(cached);
        assertEquals(created.getId(), cached.getId());
        assertEquals(created.getName(), cached.getName());
    }

    @Test
    void shouldThrowExceptionWhenCreateUserWithExistingEmail() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto created = userService.createUser(userDto);
        UserDto userDtoException = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        assertThrows(UserAlreadyExists.class, () -> userService.createUser(userDtoException));
    }

    @Test
    void shouldGetUserById() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto created = userService.createUser(userDto);
        UserDto createdFromService = userService.getUserById(created.getId());
        Optional<User> createdFromDb = jpaUserRepository.getUserById(created.getId());
        assertTrue(createdFromDb.isPresent());
        assertEquals(createdFromDb.get().getId(), createdFromService.getId());
    }

    @Test
    void shouldGetUserByIdException() {
        Long id = 5L;
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
    }

    @Test
    void shouldGetUserByIdFromCache() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto created = userService.createUser(userDto);
        Cache cache = cacheManager.getCache("users");
        assertNotNull(cache);
        UserDto cached = cache.get(created.getId(), UserDto.class);
        assertNotNull(cached);
        assertEquals(created.getId(), cached.getId());
        assertEquals(created.getName(), cached.getName());
    }

    @Test
    void shouldGetAllUsers() {
        UserDto firstDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto firstCreated = userService.createUser(firstDto);
        UserDto secondDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastyaKrivchik@gmail.com",
                new ArrayList<>());
        UserDto secondCreated = userService.createUser(secondDto);
        UserDto thirdDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "anastasia@gmail.com",
                new ArrayList<>());
        UserDto thirdCreated = userService.createUser(thirdDto);
        Pageable pageable = PageRequest.of(0, 1);
        Page<UserDto> page = userService.getAllUsers(pageable);
        assertEquals(1, page.getContent().size());
        assertEquals(3, page.getTotalElements());
        assertEquals(3, page.getTotalPages());
    }

    @Test
    void shouldGetUserByEmail() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto created = userService.createUser(userDto);
        UserDto createdFromService = userService.getUserByEmail(created.getEmail());
        Optional<User> createdFromDb = jpaUserRepository.findUserByEmail(created.getEmail());
        assertTrue(createdFromDb.isPresent());
        assertEquals(createdFromService.getEmail(), createdFromDb.get().getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserWithEmailNotFound() {
        assertThrows(UserEmailNotFoundException.class, () -> userService.getUserByEmail("test@gmail.com"));
    }

    @Test
    void shouldGetUserByEmailFromCache() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto created = userService.createUser(userDto);
        UserDto createdFromService = userService.getUserByEmail(created.getEmail());
        Cache cache = cacheManager.getCache("users");
        assertNotNull(cache);
        UserDto cached = cache.get(created.getId(), UserDto.class);
        assertNotNull(cached);
        assertEquals(cached.getEmail(), created.getEmail());
    }

    @Test
    void shouldUpdateUserWithoutCardsById() {
        UserDto userDtoForUpdate = new UserDto(
                null,
                "Milana",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto createdForUpdate = userService.createUser(userDtoForUpdate);
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastyaKrivchik@gmail.com",
                new ArrayList<>());
        UserDto created = userService.createUser(userDto);
        UserDto updated = userService.updateUserById(createdForUpdate.getId(), created);
        assertEquals("Anastasia", updated.getName());
        assertTrue(updated.getCards().isEmpty());
    }

    void shouldUpdateUserWithCardsById() {
        UserDto userDtoForUpdate = new UserDto(
                null,
                "Milana",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto createdForUpdate = userService.createUser(userDtoForUpdate);
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastyaKrivchik@gmail.com",
                List.of());
        UserDto created = userService.createUser(userDto);
        CardDto cardDto1 = new CardDto(
                null,
                createdForUpdate.getId(),
                1111222233334444L,
                "Milana",
                LocalDate.of(2030, 1, 1));
        CardDto createdCardDto1 = cardService.createCard(cardDto1);
        UserDto updated = userService.updateUserById(createdForUpdate.getId(), created);
        assertEquals("Anastasia", updated.getName());
        assertFalse(updated.getCards().isEmpty());
        assertTrue(updated.getCards().stream().anyMatch(c -> c.getNumber().equals("5555666677778888")));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        UserDto updateDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@example.com",
                new ArrayList<>()
        );
        assertThrows(UserNotFoundException.class, () -> userService.updateUserById(100L, updateDto));
    }

    @Test
    void shouldPutUpdatedUserIntoCache() {
        UserDto userDto = new UserDto(
                null,
                "Milana",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "milana@example.com",
                new ArrayList<>()
        );
        UserDto created = userService.createUser(userDto);
        UserDto updateDto = new UserDto(
                created.getId(),
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "anastasia@example.com",
                new ArrayList<>()
        );
        UserDto updated = userService.updateUserById(created.getId(), updateDto);
        Cache cache = cacheManager.getCache("users");
        assertNotNull(cache);
        UserDto cached = cache.get(created.getId(), UserDto.class);
        assertNotNull(cached);
        assertEquals("Anastasia", cached.getName());
        assertEquals("anastasia@example.com", cached.getEmail());
    }

    @Test
    void shouldDeleteUserById() {
        UserDto userDto = new UserDto(
                null,
                "Milana",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "milana@example.com",
                new ArrayList<>()
        );
        UserDto created = userService.createUser(userDto);
        userService.deleteUserById(created.getId());
        assertFalse(jpaUserRepository.findById(created.getId()).isPresent());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDelete() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(100L));
    }

    @Test
    void shouldEvictUserFromCacheOnDelete() {
        UserDto userDto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@example.com",
                new ArrayList<>()
        );
        UserDto created = userService.createUser(userDto);
        userService.updateUserById(created.getId(), created);
        Cache cache = cacheManager.getCache("users");
        assertNotNull(cache);
        assertNotNull(cache.get(created.getId(), UserDto.class));
        userService.deleteUserById(created.getId());
        assertNull(cache.get(created.getId(), UserDto.class));
    }
}
