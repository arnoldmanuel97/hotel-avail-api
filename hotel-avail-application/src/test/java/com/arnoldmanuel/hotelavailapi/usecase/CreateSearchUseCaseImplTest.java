package com.arnoldmanuel.hotelavailapi.usecase;

import com.arnoldmanuel.hotelavailapi.application.usecase.CreateSearchUseCaseImpl;
import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.application.output.SearchRepositoryPort;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateSearchUseCaseImplTest {

    @Mock
    private SearchRepositoryPort searchRepositoryPort;

    @InjectMocks
    private CreateSearchUseCaseImpl createSearchUseCaseImpl;

    @Test
    void whenCreateSearch_withCorrectFormat_thenReturnVoid() {
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        );
        SearchDomain searchDomain = new SearchDomain(null, searchAvailabilityDomain);
        when(searchRepositoryPort.save(any(SearchDomain.class))).thenReturn(Mono.empty());

        StepVerifier.create(createSearchUseCaseImpl.createSearch(searchDomain))
                .expectComplete()
                .verify();
    }
}
