package com.arnoldmanuel.hotelavailapi.application.usecase;

import com.arnoldmanuel.hotelavailapi.application.output.SearchRepositoryPort;
import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.usecase.CreateSearchUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateSearchUseCaseImpl implements CreateSearchUseCase {
    private final SearchRepositoryPort searchRepositoryPort;

    public CreateSearchUseCaseImpl(SearchRepositoryPort searchRepositoryPort) {
        this.searchRepositoryPort = searchRepositoryPort;
    }

    @Override
    public Mono<Void> createSearch(SearchDomain searchDomain) {
        return searchRepositoryPort.save(searchDomain);
    }
}
