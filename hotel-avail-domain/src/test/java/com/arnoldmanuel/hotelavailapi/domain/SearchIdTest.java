package com.arnoldmanuel.hotelavailapi.domain;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SearchIdTest {

    @Test
    void whenSearchDomain_withSameAttributes_thenGenerateSameSearchId() {
        SearchDomain searchDomain1 = new SearchDomain(
                null,
                new SearchAvailabilityDomain(
                        new SearchAvailabilityDomain.Hotel("hotelId"),
                        new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                        List.of(7, 23)
                )
        );

        SearchDomain searchDomain2 = new SearchDomain(
                null,
                new SearchAvailabilityDomain(
                        new SearchAvailabilityDomain.Hotel("hotelId"),
                        new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                        List.of(7, 23)
                )
        );

        assertEquals(searchDomain1.searchId().value(), searchDomain2.searchId().value());
    }
}
