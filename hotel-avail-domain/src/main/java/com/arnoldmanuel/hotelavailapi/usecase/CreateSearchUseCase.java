package com.arnoldmanuel.hotelavailapi.usecase;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import reactor.core.publisher.Mono;

public interface CreateSearchUseCase {
    Mono<Void> createSearch(SearchDomain searchDomain);
}
