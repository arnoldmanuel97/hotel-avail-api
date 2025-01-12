package com.arnoldmanuel.hotelavailapi.application.usecase;

import com.arnoldmanuel.hotelavailapi.application.output.SearchRepositoryPort;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.domain.SimilarSearchDomain;
import com.arnoldmanuel.hotelavailapi.usecase.CountSearchUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CountSearchUseCaseImpl implements CountSearchUseCase {

    private final SearchRepositoryPort searchRepository;

    public CountSearchUseCaseImpl(SearchRepositoryPort searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public Mono<SimilarSearchDomain> countSearch(SearchId searchId) {
        return searchRepository.countSearch(searchId);
    }
}
