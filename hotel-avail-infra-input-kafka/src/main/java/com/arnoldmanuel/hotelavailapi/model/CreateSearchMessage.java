package com.arnoldmanuel.hotelavailapi.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateSearchMessage(
        @NotNull(message = "El campo searchId es obligatorio")
        String searchId,
        @NotBlank(message = "El campo hotelId es obligatorio")
        String hotelId,
        @NotNull(message = "El campo checkIn es obligatorio")
        LocalDate checkIn,
        @NotNull(message = "El campo checkOut es obligatorio")
        LocalDate checkOut,
        @NotEmpty(message = "La lista de edades no puede estar vacía")
        List<Integer> ages) {
}
