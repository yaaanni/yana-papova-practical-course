package com.example.demo.dto.mapping;

import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CardMapping.class})
public interface UserMapping {
    @Mapping(source = "cards", target = "cards")
    UserDto toDto(User user);
    @Mapping(source = "cards", target = "cards")
    User toEntity(UserDto dto);
}
