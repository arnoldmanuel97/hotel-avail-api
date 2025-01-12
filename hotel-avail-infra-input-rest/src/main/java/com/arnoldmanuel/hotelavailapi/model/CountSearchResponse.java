package com.arnoldmanuel.hotelavailapi.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record CountSearchResponse(
        String searchId,
        Search search,
        long count
) {
    public record Search(
            String hotelId,
            @DateTimeFormat(pattern = "dd/MM/yyyy")
            LocalDate checkIn,
            @DateTimeFormat(pattern = "dd/MM/yyyy")
            LocalDate checkOut,
            List<Integer> ages
    ) {
    }
}
