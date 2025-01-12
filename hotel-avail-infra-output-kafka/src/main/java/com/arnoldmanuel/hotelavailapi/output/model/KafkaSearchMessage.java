package com.arnoldmanuel.hotelavailapi.output.model;

import java.time.LocalDate;
import java.util.List;

public record KafkaSearchMessage(
        String searchId,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
}
