package com.example.demo.service.user;

import com.example.demo.dto.CardDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapping.CardMapping;
import com.example.demo.dto.mapping.UserMapping;
import com.example.demo.entities.Card;
import com.example.demo.entities.User;
import com.example.demo.exception.UserAlreadyExists;
import com.example.demo.exception.UserEmailNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import com.example.demo.service.User.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private JpaCardRepository jpaCardRepository;
    @Mock
    private JpaUserRepository jpaUserRepository;
    @Mock
    private UserMapping userMapping;
    @Mock
    private CardMapping cardMapping;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUserWithoutCards() {
        UserDto dto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());

        User userEntity = new User(
                1L,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        when(userMapping.toEntity(dto)).thenReturn(userEntity);
        when(userMapping.toDto(userEntity)).thenReturn(dto);
        when(jpaUserRepository.save(userEntity)).thenReturn(userEntity);
        assertEquals(dto, userService.createUser(dto));
        verify(jpaUserRepository).save(userEntity);
    }

    @Test
    void shouldCreateUserWithCards() {
        CardDto cardDto = new CardDto(
                null,
                1L,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3));

        UserDto dto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                List.of(cardDto));

        User userEntity = new User(
                1L,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());

        Card cardEntity = new Card(
                1L,
                userEntity,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3));
        when(userMapping.toEntity(dto)).thenReturn(userEntity);
        when(jpaUserRepository.save(userEntity)).thenReturn(userEntity);
        when(cardMapping.toEntity(cardDto)).thenReturn(cardEntity);
        when(userMapping.toDto(userEntity)).thenReturn(dto);
        assertEquals(dto, userService.createUser(dto));
        verify(jpaUserRepository).save(userEntity);
        verify(jpaCardRepository).saveAll(List.of(cardEntity));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        UserDto dto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        User userEntity = new User(
                1L,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto dtoException = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        when(jpaUserRepository.findUserByEmail(dto.getEmail())).thenReturn(Optional.of(userEntity));
        assertThrows(UserAlreadyExists.class, () -> userService.createUser(dtoException));
    }

    @Test
    void shouldGetUserById() {
        Long id = 5L;
        UserDto dto = new UserDto(
                id,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());

        User userEntity = new User(
                id,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        when(jpaUserRepository.getUserById(id)).thenReturn(Optional.of(userEntity));
        when(userMapping.toDto(userEntity)).thenReturn(dto);
        assertEquals(dto, userService.getUserById(id));
    }

    @Test
    void getUserByIdException() {
        Long id = 5L;
        when(jpaUserRepository.getUserById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
    }

    @Test
    void shouldReturnPagedUsers() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());

        User user1 = new User(
                1L,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        User user2 = new User(
                2L,
                "Nastya",
                "Ivanova",
                LocalDate.of(2006, 5, 10),
                "ivanova@gmail.com",
                new ArrayList<>());

        UserDto dto1 = new UserDto(
                1L,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto dto2 = new UserDto(
                2L,
                "Nastya",
                "Ivanova",
                LocalDate.of(2006, 5, 10),
                "ivanova@gmail.com",
                new ArrayList<>());

        Page<User> userPage = new PageImpl<>(List.of(user1, user2), pageable, 2);

        when(jpaUserRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapping.toDto(user1)).thenReturn(dto1);
        when(userMapping.toDto(user2)).thenReturn(dto2);

        Page<UserDto> result = userService.getAllUsers(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));
        assertEquals(2, result.getTotalElements());

        verify(jpaUserRepository).findAll(pageable);
        verify(userMapping).toDto(user1);
        verify(userMapping).toDto(user2);
    }

    @Test
    void shouldGetUserByEmail() {
        String email = "nastya@gmail.com";
        UserDto dto = new UserDto(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());

        User userEntity = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        when(jpaUserRepository.findUserByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userMapping.toDto(userEntity)).thenReturn(dto);
        assertEquals(dto, userService.getUserByEmail(email));
    }

    @Test
    void getUserByEmailException() {
        String email = "nastya@gmail.com";
        when(jpaUserRepository.findUserByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UserEmailNotFoundException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    void shouldUpdateUserWithoutCardsById() {
        Long id = 5L;
        UserDto dto = new UserDto(
                null,
                "Milana",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        UserDto dtoForUpdate = new UserDto(
                id,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());

        User userEntityForUpdate = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());

        when(jpaUserRepository.getUserById(id)).thenReturn(Optional.of(userEntityForUpdate));
        when(userMapping.toDto(userEntityForUpdate)).thenReturn(dto);
        assertEquals(dto, userService.updateUserById(id, dtoForUpdate));
        verify(jpaUserRepository).getUserById(id);
        verify(userMapping).toDto(userEntityForUpdate);
        verify(jpaUserRepository, never()).save(any());
        verify(jpaCardRepository, never()).saveAll(anyList());
    }

    @Test
    void shouldUpdateUserWithCardsById() {
        Long id = 5L;
        CardDto cardDto = new CardDto(
                null,
                id,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3));

        UserDto dto = new UserDto(
                null,
                "Milana",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                List.of(cardDto));

        UserDto dtoForUpdate = new UserDto(
                id,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                List.of(cardDto));

        User userEntityForUpdate = new User(
                id,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());

        Card cardEntity = new Card(
                null,
                userEntityForUpdate,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3));
        when(jpaUserRepository.getUserById(id)).thenReturn(Optional.of(userEntityForUpdate));
        when(userMapping.toDto(userEntityForUpdate)).thenReturn(dto);
        when(cardMapping.toEntity(cardDto)).thenReturn(cardEntity);
        assertEquals(dto, userService.updateUserById(id, dtoForUpdate));

        verify(jpaUserRepository).getUserById(id);
        verify(cardMapping).toEntity(cardDto);
        verify(userMapping).toDto(userEntityForUpdate);
    }

    @Test
    void updateUserByIdException() {
        Long id = 5L;
        UserDto dtoForUpdate = new UserDto(
                1L,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        when(jpaUserRepository.getUserById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUserById(id, dtoForUpdate));
    }

    @Test
    void shouldDeleteUserById() {
        Long id = 5L;
        User userEntity = new User(
                id,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>());
        when(jpaUserRepository.getUserById(id)).thenReturn(Optional.of(userEntity));
        userService.deleteUserById(id);
        verify(jpaUserRepository).deleteById(id);
    }

    @Test
    void deleteUserByIdException() {
        Long id = 5L;
        when(jpaUserRepository.getUserById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(id));
    }

}
