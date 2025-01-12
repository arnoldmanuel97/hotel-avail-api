package com.arnoldmanuel.hotelavailapi.api;

import com.arnoldmanuel.hotelavailapi.mapper.SearchHotelAvailabilityMapper;
import com.arnoldmanuel.hotelavailapi.model.CountSearchResponse;
import com.arnoldmanuel.hotelavailapi.model.SearchHotelAvailabilityRequest;
import com.arnoldmanuel.hotelavailapi.model.SearchHotelAvailabilityResponse;
import com.arnoldmanuel.hotelavailapi.usecase.CountSearchUseCase;
import com.arnoldmanuel.hotelavailapi.usecase.SendSearchUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RequestMapping("/hotel-avail-api")
@RestController
public class SearchHotelAvailabilityController {

    private final SendSearchUseCase sendSearchUseCase;
    private final CountSearchUseCase countSearchUseCase;
    private final SearchHotelAvailabilityMapper searchHotelAvailabilityMapper;

    public SearchHotelAvailabilityController(SendSearchUseCase sendSearchUseCase, CountSearchUseCase countSearchUseCase,
                                             SearchHotelAvailabilityMapper searchHotelAvailabilityMapper) {
        this.sendSearchUseCase = sendSearchUseCase;
        this.countSearchUseCase = countSearchUseCase;
        this.searchHotelAvailabilityMapper = searchHotelAvailabilityMapper;
    }

    @PostMapping("/search")
    public Mono<SearchHotelAvailabilityResponse> search(
            @RequestBody @Valid final SearchHotelAvailabilityRequest request) {
        return sendSearchUseCase.send(searchHotelAvailabilityMapper.toDomain(request))
                .map(searchHotelAvailabilityMapper::fromDomain);
    }

    @GetMapping("/count/{searchId}")
    public Mono<CountSearchResponse> count(@PathVariable String searchId) {
        return countSearchUseCase.countSearch(searchHotelAvailabilityMapper.toDomain(searchId))
                .map(searchHotelAvailabilityMapper::fromDomain);
    }
}
