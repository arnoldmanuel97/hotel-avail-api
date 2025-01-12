package com.arnoldmanuel.hotelavailapi.usecase;

import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.domain.SimilarSearchDomain;
import reactor.core.publisher.Mono;

public interface CountSearchUseCase {
    Mono<SimilarSearchDomain> countSearch(SearchId searchId);
}
