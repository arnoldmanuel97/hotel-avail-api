package com.arnoldmanuel.hotelavailapi.application.output;

import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import reactor.core.publisher.Mono;

public interface SearchMessagingPort {
    Mono<SearchId> send(SearchDomain search);
}
