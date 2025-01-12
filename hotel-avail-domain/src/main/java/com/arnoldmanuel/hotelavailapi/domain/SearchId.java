package com.arnoldmanuel.hotelavailapi.domain;

import com.arnoldmanuel.hotelavailapi.exceptions.DomainException;

import java.util.HashSet;
import java.util.Objects;

public record SearchId(String value) {

    public SearchId {
        if (value == null || value.isEmpty()) {
            throw new DomainException("SearchId no puede ser nulo o vacío.");
        }
    }

    public static SearchId generateFrom(SearchAvailabilityDomain search) {
        int hash = Objects.hash(
                search.hotel().hotelId(),
                search.dateRange().checkIn(),
                search.dateRange().checkOut(),
                new HashSet<>(search.ages())
        );
        return new SearchId(String.valueOf(hash));
    }

    @Override
    public String toString() {
        return value;
    }
}
