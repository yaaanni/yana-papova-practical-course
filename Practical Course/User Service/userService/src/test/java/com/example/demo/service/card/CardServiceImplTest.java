package com.example.demo.service.card;

import com.example.demo.dto.CardDto;
import com.example.demo.dto.mapping.CardMapping;
import com.example.demo.entities.Card;
import com.example.demo.entities.User;
import com.example.demo.exception.CardNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import com.example.demo.service.Card.CardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private JpaCardRepository jpaCardRepository;

    @Mock
    private JpaUserRepository jpaUserRepository;

    @Mock
    private CardMapping cardMapping;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void createCardSuccessfullyException() {
        CardDto dto = new CardDto(
                null,
                1L,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3)
        );
        User user = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 03, 04),
                "nastya@gmail.com",
                new ArrayList<>()
        );
        Card card = new Card(
                null,
                user,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 10, 3)
        );
        when(cardMapping.toEntity(dto)).thenReturn(card);
        when(jpaUserRepository.getUserById(dto.getUserId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> cardService.createCard(dto));
    }

    @Test
    void shouldCreateCardSuccessfully() {
        CardDto dto = new CardDto(
                null,
                1L,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3)
        );
        User user = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 03, 04),
                "nastya@gmail.com",
                new ArrayList<>()
        );
        Card card = new Card(
                null,
                user,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 10, 3)
        );
        when(cardMapping.toEntity(dto)).thenReturn(card);
        when(jpaUserRepository.getUserById(dto.getUserId())).thenReturn(Optional.of(user));
        when(cardMapping.toDto(card)).thenReturn(dto);
        when(jpaCardRepository.save(card)).thenReturn(card);
        assertEquals(dto, cardService.createCard(dto));
    }

    @Test
    void getCardByIdException() {
        Long id = 5L;
        CardDto dto = new CardDto(
                null,
                1L,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3)
        );
        User user = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 03, 04),
                "nastya@gmail.com",
                new ArrayList<>()
        );
        Card card = new Card(
                6L,
                user,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 10, 3)
        );
        when(jpaCardRepository.getCardById(id)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(id));
    }

    @Test
    void shouldGetCardById() {
        Long id = 5L;
        CardDto dto = new CardDto(
                null,
                1L,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3)
        );
        User user = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 03, 04),
                "nastya@gmail.com",
                new ArrayList<>()
        );
        Card card = new Card(
                id,
                user,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 10, 3)
        );
        when(jpaCardRepository.getCardById(id)).thenReturn(Optional.of(card));
        when(cardMapping.toDto(card)).thenReturn(dto);
        assertEquals(dto, cardService.getCardById(id));
    }

    @Test
    void shouldReturnPagedCards() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());

        Card card1 = new Card(1L, null, 1111222233334444L, "Anastasia", LocalDate.of(2026, 12, 31));
        Card card2 = new Card(2L, null, 5555666677778888L, "Nastya", LocalDate.of(2027, 6, 30));

        CardDto dto1 = new CardDto(1L, 5L, 1111222233334444L, "Anastasia", LocalDate.of(2026, 12, 31));
        CardDto dto2 = new CardDto(2L, 5L, 5555666677778888L, "Nastya", LocalDate.of(2027, 6, 30));

        Page<Card> cardPage = new PageImpl<>(List.of(card1, card2), pageable, 2);

        when(jpaCardRepository.findAll(pageable)).thenReturn(cardPage);
        when(cardMapping.toDto(card1)).thenReturn(dto1);
        when(cardMapping.toDto(card2)).thenReturn(dto2);

        Page<CardDto> result = cardService.getAllCards(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void shouldDeleteCardById() {
        Long id = 5L;
        User user = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 03, 04),
                "nastya@gmail.com",
                new ArrayList<>()
        );
        Card card = new Card(
                id,
                user,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 10, 3)
        );
        when(jpaCardRepository.getCardById(id)).thenReturn(Optional.of(card));
        cardService.deleteCardById(id);
        verify(jpaCardRepository).getCardById(id);
        verify(jpaCardRepository).deleteById(id);
    }

    @Test
    void deleteCardByIdException() {
        Long id = 5L;
        User user = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 03, 04),
                "nastya@gmail.com",
                new ArrayList<>()
        );
        Card card = new Card(
                7L,
                user,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 10, 3)
        );
        when(jpaCardRepository.getCardById(id)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.deleteCardById(id));
    }

    @Test
    void shouldUpdateCardTest() {
        Long id = 5L;
        CardDto dto = new CardDto(
                null,
                1L,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3)
        );
        User user = new User(
                null,
                "Anastasia",
                "Krivchik",
                LocalDate.of(2007, 3, 4),
                "nastya@gmail.com",
                new ArrayList<>()
        );
        Card card = new Card(
                5L,
                user,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 10, 3)
        );
        Card cardForUpdate = new Card(
                5L,
                user,
                1234123412341234L,
                "Anastasia Krivchik",
                LocalDate.of(2026, 10, 3)
        );

        when(jpaCardRepository.getCardById(id)).thenReturn(Optional.of(cardForUpdate));
        when(jpaUserRepository.getUserById(dto.getUserId())).thenReturn(Optional.of(user));
        when(jpaCardRepository.save(cardForUpdate)).thenReturn(card);
        when(cardMapping.toDto(card)).thenReturn(dto);

        CardDto result = cardService.updateCard(id, dto);

        assertEquals(dto, result);

        verify(jpaCardRepository).getCardById(id);
        verify(jpaUserRepository).getUserById(dto.getUserId());
        verify(jpaCardRepository).save(cardForUpdate);
        verify(cardMapping).toDto(card);
    }

    @Test
    void shouldThrowExceptionWhenCardNotFound() {
        Long id = 5L;
        CardDto dto = new CardDto(
                null,
                1L,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3)
        );

        when(jpaCardRepository.getCardById(id)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.updateCard(id, dto));

        verify(jpaCardRepository).getCardById(id);
        verify(jpaUserRepository, never()).getUserById(anyLong());
        verify(jpaCardRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Long id = 5L;
        CardDto dto = new CardDto(
                null,
                1L,
                1234123412341234L,
                "Anastasia",
                LocalDate.of(2026, 10, 3)
        );

        Card existingCard = new Card(
                id,
                null,
                1234123412341234L,
                "Old Holder",
                LocalDate.of(2025, 12, 31)
        );

        when(jpaCardRepository.getCardById(id)).thenReturn(Optional.of(existingCard));
        when(jpaUserRepository.getUserById(dto.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cardService.updateCard(id, dto));
    }


}
