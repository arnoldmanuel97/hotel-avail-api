package com.arnoldmanuel.hotelavailapi.messaging;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.mapper.CreateSearchMapper;
import com.arnoldmanuel.hotelavailapi.model.CreateSearchMessage;
import com.arnoldmanuel.hotelavailapi.usecase.CreateSearchUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchMessagingConsumerTest {

    @InjectMocks
    SearchMessagingConsumer searchMessagingConsumer;

    @Mock
    CreateSearchMapper createSearchMapper;

    @Mock
    CreateSearchUseCase createSearchUseCase;

    @Test
    void whenCreateSearch_withValidCreateSearchMessage_thenCreateSearch() {
        CreateSearchMessage createSearchMessage = new CreateSearchMessage(
                "1", "1", LocalDate.now(), LocalDate.now().plusDays(1), List.of(1)
        );
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("1"), new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(1)), List.of(1)
        );
        SearchDomain searchDomain = new SearchDomain(null, searchAvailabilityDomain);
        when(createSearchMapper.toDomain(createSearchMessage)).thenReturn(searchDomain);
        when(createSearchUseCase.createSearch(any(SearchDomain.class))).thenReturn(Mono.empty());

        Message<CreateSearchMessage> message = MessageBuilder.withPayload(createSearchMessage).build();

        searchMessagingConsumer.createSearch().accept(Flux.just(message));

        verify(createSearchUseCase).createSearch(any(SearchDomain.class));
    }
}
