package com.arnoldmanuel.hotelavailapi.usecase;

import com.arnoldmanuel.hotelavailapi.application.output.SearchRepositoryPort;
import com.arnoldmanuel.hotelavailapi.application.usecase.CountSearchUseCaseImpl;
import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.domain.SimilarSearchDomain;
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
class CountSearchUseCaseImplTest {

    @Mock
    private SearchRepositoryPort searchRepositoryPort;

    @InjectMocks
    private CountSearchUseCaseImpl countSearchUseCaseImpl;

    @Test
    void whenCountSearch_withValidSearchId_thenReturnSimilarSearchDomain() {
        SearchId searchId = new SearchId("123");
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("1"), new SearchAvailabilityDomain.DateRange(LocalDate.now(),
                LocalDate.now().plusDays(1)), List.of(1));
        SimilarSearchDomain similarSearchDomain = new SimilarSearchDomain(searchId, searchAvailabilityDomain, 1L);

        when(searchRepositoryPort.countSearch(any(SearchId.class))).thenReturn(Mono.just(similarSearchDomain));

        StepVerifier.create(countSearchUseCaseImpl.countSearch(searchId))
                .expectNext(similarSearchDomain)
                .verifyComplete();
    }
}
