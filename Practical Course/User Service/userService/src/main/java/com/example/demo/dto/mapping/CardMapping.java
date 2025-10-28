package com.example.demo.dto.mapping;

import com.example.demo.dto.CardDto;
import com.example.demo.entities.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapping {
    @Mapping(source = "user.id", target = "userId")
    CardDto toDto(Card entity);
    @Mapping(source = "userId", target = "user.id")
    Card toEntity(CardDto dto);
}
