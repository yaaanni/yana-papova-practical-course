package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Positive(message = "User ID must be a positive number")
    private Long id;
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Surname is required")
    private String surname;
    @NotNull(message = "Birthday date is required")
    @Past(message = "The birthday date must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;
    @NotNull(message = "Email is required")
    private String email;
    private List<CardDto> cards = new ArrayList<>();
}
