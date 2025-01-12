package com.arnoldmanuel.hotelavailapi.usecase;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import reactor.core.publisher.Mono;

public interface SendSearchUseCase {
    Mono<SearchId> send(SearchDomain searchDomain);

}
