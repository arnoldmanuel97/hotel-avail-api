package com.arnoldmanuel.hotelavailapi.domain;

import java.time.LocalDate;
import java.util.List;

import com.arnoldmanuel.hotelavailapi.exceptions.InvalidAgesException;
import com.arnoldmanuel.hotelavailapi.exceptions.InvalidDateRangeException;
import com.arnoldmanuel.hotelavailapi.exceptions.InvalidSearchException;

public record SearchAvailabilityDomain(
        Hotel hotel,
        DateRange dateRange,
        List<Integer> ages) {

    public SearchAvailabilityDomain {
        validateDateRange(dateRange);
        validateAges(List.copyOf(ages));
        validateHotelId(hotel.hotelId());
    }

    public record DateRange(
            LocalDate checkIn,
            LocalDate checkOut) {

    }

    public record Hotel(
            String hotelId) {

    }

    private static void validateDateRange(DateRange dateRange) {
        if (dateRange.checkOut().isBefore(dateRange.checkIn())) {
            throw new InvalidDateRangeException("El Check-out debe ser antes que el check-in");
        }
    }

    private static void validateAges(List<Integer> ages) {
        if (ages == null || ages.isEmpty()) {
            throw new InvalidAgesException("Por lo menos una edad es requerida");
        }
        if (ages.stream().anyMatch(age -> age < 0)) {
            throw new InvalidAgesException("Las edades no pueden ser negativas");
        }
    }

    private static void validateHotelId(String hotelId) {
        if (hotelId == null || hotelId.isEmpty()) {
            throw new InvalidSearchException("El hotelId es requerido");
        }
    }
}
