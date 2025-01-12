package com.arnoldmanuel.hotelavailapi.messaging;

import com.arnoldmanuel.hotelavailapi.application.output.SearchRepositoryPort;
import com.arnoldmanuel.hotelavailapi.application.usecase.CreateSearchUseCaseImpl;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.mapper.CreateSearchMapperImpl;
import com.arnoldmanuel.hotelavailapi.model.CreateSearchMessage;
import com.arnoldmanuel.hotelavailapi.usecase.CreateSearchUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@Import({TestChannelBinderConfiguration.class})
@SpringBootTest(classes = {
        SearchMessagingConsumer.class,
        CreateSearchUseCaseImpl.class,
        CreateSearchMapperImpl.class})
class SearchMessagingConsumerIT {

    @Autowired
    SearchMessagingConsumer searchMessagingConsumer;

    @Autowired
    InputDestination inputDestination;

    @SpyBean
    CreateSearchUseCase createSearchUseCase;

    @MockBean
    SearchRepositoryPort searchRepositoryPort;

    @Test
    void whenCreateSearch_withValidCreateSearchMessage_thenCreateSearch() {
        when(searchRepositoryPort.save(any(SearchDomain.class))).thenReturn(Mono.empty());
        var checkIn = LocalDate.of(2024, 10, 10);
        var checkOut = LocalDate.of(2024, 10, 14);
        var createSearchMessage = new CreateSearchMessage("705513100", "hotelId",
                checkIn, checkOut, List.of(7, 23));
        Message<CreateSearchMessage> message = MessageBuilder.withPayload(createSearchMessage).build();

        inputDestination.send(message, "hotel_availability_searches");
        ArgumentCaptor<SearchDomain> searchCaptor = ArgumentCaptor.forClass(SearchDomain.class);
        verify(createSearchUseCase, timeout(5000)).createSearch(searchCaptor.capture());

        SearchDomain searchDomain = searchCaptor.getValue();
        System.out.println(LocalDate.now());
        assertEquals(createSearchMessage.searchId(), searchDomain.searchId().value());
        assertEquals(createSearchMessage.hotelId(), searchDomain.search().hotel().hotelId());
        assertEquals(createSearchMessage.checkIn(), searchDomain.search().dateRange().checkIn());
        assertEquals(createSearchMessage.checkOut(), searchDomain.search().dateRange().checkOut());
        assertEquals(createSearchMessage.ages(), searchDomain.search().ages());
    }
}
