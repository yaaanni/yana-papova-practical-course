package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private Long id;
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Long userId;
    @NotNull(message = "Card number is required")
    @NotNull(message = "Card number is required")
    @Min(value = 1000000000000000L, message = "Card number must be 16 digits")
    @Max(value = 9999999999999999L, message = "Card number must be 16 digits")
    private Integer number;
    @NotNull(message = "Holder is required")
    private String holder;
    @NotNull(message = "Expiration date is required")
    @Future(message = "The date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
}
