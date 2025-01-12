package com.arnoldmanuel.hotelavailapi.application.output;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.domain.SimilarSearchDomain;
import reactor.core.publisher.Mono;

public interface SearchRepositoryPort {
    Mono<Void> save(SearchDomain searchDomain);

    Mono<SimilarSearchDomain> countSearch(SearchId searchId);
}
