package com.arnoldmanuel.hotelavailapi.usecase;

import com.arnoldmanuel.hotelavailapi.application.usecase.SendSearchUseCaseImpl;
import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.application.output.SearchMessagingPort;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendSearchUseCaseImplTest {

    @Mock
    private SearchMessagingPort searchMessagingPort;

    @InjectMocks
    private SendSearchUseCaseImpl sendSearchUseCaseImpl;

    @Test
    void whenSendSearch_withCorrectFormat_thenReturnVoid() {
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        );
        SearchDomain searchDomain = new SearchDomain(
                new SearchId("123"),
                searchAvailabilityDomain
        );
        when(searchMessagingPort.send(any(SearchDomain.class))).thenReturn(Mono.just(new SearchId("123")));

        StepVerifier.create(sendSearchUseCaseImpl.send(searchDomain))
                .assertNext(searchId -> assertEquals(searchId.value(), "123"))
                .verifyComplete();
    }
}
