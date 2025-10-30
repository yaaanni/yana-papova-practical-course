package com.example.demo.service.Card;

import com.example.demo.dto.CardDto;
import com.example.demo.dto.mapping.CardMapping;
import com.example.demo.entities.Card;
import com.example.demo.entities.User;
import com.example.demo.exception.CardAlreadyExist;
import com.example.demo.exception.CardNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final JpaCardRepository jpaCardRepository;
    private final JpaUserRepository jpaUserRepository;
    private final CardMapping cardMapping;

    @Override
    public CardDto createCard(CardDto dto) {
        if (jpaCardRepository.existsByNumber(dto.getNumber())) {
            throw new CardAlreadyExist(dto.getNumber());
        }
        Card card = cardMapping.toEntity(dto);
        User user = jpaUserRepository.getUserById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));
        card.setUser(user);
        return cardMapping.toDto(jpaCardRepository.save(card));
    }

    @Override
    public CardDto getCardById(Long id) {
        Card card = jpaCardRepository.getCardById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        return cardMapping.toDto(card);
    }

    @Override
    public Page<CardDto> getAllCards(Pageable pageable) {
        Page<Card> cards = jpaCardRepository.findAll(pageable);
        return cards
                .map(card -> cardMapping.toDto(card));
    }

    @Override
    @Transactional
    public void deleteCardById(Long id) {
        Card card = jpaCardRepository.getCardById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        jpaCardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CardDto updateCard(Long id, CardDto dto) {
        Card cardForUpdate = jpaCardRepository.getCardById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        User user = jpaUserRepository.getUserById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));
        cardForUpdate.setUser(user);
        cardForUpdate.setHolder(dto.getHolder());
        cardForUpdate.setNumber(dto.getNumber());
        return cardMapping.toDto(jpaCardRepository.save(cardForUpdate));
    }

}
