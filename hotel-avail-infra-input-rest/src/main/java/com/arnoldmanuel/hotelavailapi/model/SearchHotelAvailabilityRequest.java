package com.arnoldmanuel.hotelavailapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record SearchHotelAvailabilityRequest(
    @NotBlank(message = "El campo hotelId es obligatorio")
    String hotelId,
    @NotNull(message = "El campo checkIn es obligatorio")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate checkIn,
    @NotNull(message = "El campo checkOut es obligatorio")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate checkOut,
    @NotEmpty(message = "La lista de edades no puede estar vacía")
    List<Integer> ages
) {
}
