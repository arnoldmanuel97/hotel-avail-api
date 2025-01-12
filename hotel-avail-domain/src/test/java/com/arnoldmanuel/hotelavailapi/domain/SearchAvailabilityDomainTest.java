package com.arnoldmanuel.hotelavailapi.domain;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arnoldmanuel.hotelavailapi.exceptions.InvalidAgesException;
import com.arnoldmanuel.hotelavailapi.exceptions.InvalidDateRangeException;
import com.arnoldmanuel.hotelavailapi.exceptions.InvalidSearchException;

public class SearchAvailabilityDomainTest {

    @Test
    void whenSearchDomain_withValidSearchDomain_thenCreateSearchDomain() {
        var searchDomain = assertDoesNotThrow(() -> new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        ));
        assertNotNull(searchDomain);
    }

    @Test
    void whenSearchDomain_withInvalidDateRange_thenThrowInvalidDateRangeException() {
        assertThrows(InvalidDateRangeException.class, () -> new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now().plusDays(4), LocalDate.now()),
                List.of(7, 23)
        ));
    }

    @Test
    void whenSearchDomain_withEmptyAges_thenThrowInvalidAgesException() {
        assertThrows(InvalidAgesException.class, () -> new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of()
        ));
    }

    @Test
    void whenSearchDomain_withNegativeAges_thenThrowInvalidAgesException() {
        assertThrows(InvalidAgesException.class, () -> new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(-1, 23)
        ));
    }

    @Test
    void whenSearchDomain_withNullHotelId_thenThrowInvalidSearchException() {
        assertThrows(InvalidSearchException.class, () -> new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel(null),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        ));
    }

    @Test
    void whenSearchDomain_withEmptyHotelId_thenThrowInvalidSearchException() {
        assertThrows(InvalidSearchException.class, () -> new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel(""),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        ));
    }
}
