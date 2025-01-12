package com.arnoldmanuel.hotelavailapi.application.usecase;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.application.output.SearchMessagingPort;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.usecase.SendSearchUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SendSearchUseCaseImpl implements SendSearchUseCase {
    private final SearchMessagingPort searchMessagingPort;

    public SendSearchUseCaseImpl(SearchMessagingPort searchMessagingPort) {
        this.searchMessagingPort = searchMessagingPort;
    }

    @Override
    public Mono<SearchId> send(SearchDomain searchDomain) {
        return searchMessagingPort.send(searchDomain);
    }
}
