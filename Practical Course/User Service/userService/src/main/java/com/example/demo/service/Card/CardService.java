package com.example.demo.service.Card;

import com.example.demo.dto.CardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {
    CardDto createCard(CardDto dto);

    CardDto getCardById(Long id);

    Page<CardDto> getAllCards(Pageable pageable);

    void deleteCardById(Long id);

    CardDto updateCard(Long id, CardDto dto);
}
