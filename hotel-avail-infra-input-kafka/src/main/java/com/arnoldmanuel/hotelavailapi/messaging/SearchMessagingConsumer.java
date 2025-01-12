package com.arnoldmanuel.hotelavailapi.messaging;

import com.arnoldmanuel.hotelavailapi.mapper.CreateSearchMapper;
import com.arnoldmanuel.hotelavailapi.model.CreateSearchMessage;
import com.arnoldmanuel.hotelavailapi.usecase.CreateSearchUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Component
public class SearchMessagingConsumer {
    private final CreateSearchUseCase createSearchUseCase;
    private final CreateSearchMapper createSearchMapper;

    public SearchMessagingConsumer(CreateSearchUseCase createSearchUseCase, CreateSearchMapper createSearchMapper) {
        this.createSearchUseCase = createSearchUseCase;
        this.createSearchMapper = createSearchMapper;
    }

    @Bean
    public Consumer<Flux<Message<CreateSearchMessage>>> createSearch() {
        return messages -> messages
                .map(Message::getPayload)
                .map(createSearchMapper::toDomain)
                .doOnNext(createSearchUseCase::createSearch)
                .subscribe();
    }

}
