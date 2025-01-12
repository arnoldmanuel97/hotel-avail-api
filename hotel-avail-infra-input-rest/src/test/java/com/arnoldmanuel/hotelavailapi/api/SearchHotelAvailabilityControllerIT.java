package com.arnoldmanuel.hotelavailapi.api;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.arnoldmanuel.hotelavailapi.application.output.SearchMessagingPort;
import com.arnoldmanuel.hotelavailapi.application.output.SearchRepositoryPort;
import com.arnoldmanuel.hotelavailapi.application.usecase.CountSearchUseCaseImpl;
import com.arnoldmanuel.hotelavailapi.application.usecase.SendSearchUseCaseImpl;
import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.domain.SimilarSearchDomain;
import com.arnoldmanuel.hotelavailapi.mapper.SearchHotelAvailabilityMapper;
import com.arnoldmanuel.hotelavailapi.mapper.SearchHotelAvailabilityMapperImpl;
import com.arnoldmanuel.hotelavailapi.model.CountSearchResponse;
import com.arnoldmanuel.hotelavailapi.model.SearchHotelAvailabilityRequest;
import com.arnoldmanuel.hotelavailapi.model.SearchHotelAvailabilityResponse;
import com.arnoldmanuel.hotelavailapi.usecase.CountSearchUseCase;
import com.arnoldmanuel.hotelavailapi.usecase.SendSearchUseCase;

import reactor.core.publisher.Mono;

@SpringBootTest(classes = {
    SearchHotelAvailabilityController.class,
    SendSearchUseCaseImpl.class,
    CountSearchUseCaseImpl.class,
    SearchHotelAvailabilityMapperImpl.class})
public class SearchHotelAvailabilityControllerIT {

    WebTestClient webTestClient;

    @Autowired
    SendSearchUseCase sendSearchUseCase;
    @Autowired
    CountSearchUseCase countSearchUseCase;
    @Autowired
    SearchHotelAvailabilityMapper searchHotelAvailabilityMapper;

    @MockBean
    SearchMessagingPort searchMessagingPort;

    @MockBean
    SearchRepositoryPort searchRepositoryPort;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(
                new SearchHotelAvailabilityController(
                        sendSearchUseCase,
                        countSearchUseCase,
                        searchHotelAvailabilityMapper)).build();
    }

    @Test
    void whenSearchHotelAvailability_withValidRequest_thenSearchHotelAvailability() {
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
        SearchHotelAvailabilityRequest searchHotelAvailabilityRequest = new SearchHotelAvailabilityRequest(
                "hotelId", LocalDate.now(), LocalDate.now().plusDays(1), List.of(1)
        );

        webTestClient.post().uri("/hotel-avail-api/search").bodyValue(searchHotelAvailabilityRequest).exchange()
                .expectStatus().isOk()
                .expectBody(SearchHotelAvailabilityResponse.class)
                .value(response -> {
                    assertEquals("123", response.searchId());
                });
    }

    @Test
    void whenCountSearch_withValidSearchId_thenReturnCountSearchResponse() {
        String searchId = "123";
        SearchId domainSearchId = new SearchId(searchId);
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(1)),
                List.of(1)
        );
        SimilarSearchDomain similarSearchDomain = new SimilarSearchDomain(domainSearchId,
                searchAvailabilityDomain, 5L);
        CountSearchResponse expectedResponse = new CountSearchResponse(searchId,
                new CountSearchResponse.Search("hotelId", LocalDate.now(), LocalDate.now().plusDays(1),
                        List.of(1)),
                5L
        );
        when(searchRepositoryPort.countSearch(domainSearchId)).thenReturn(Mono.just(similarSearchDomain));
        webTestClient.get()
                .uri("/hotel-avail-api/count/{searchId}", searchId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CountSearchResponse.class)
                .isEqualTo(expectedResponse);
    }
}
