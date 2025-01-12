package com.arnoldmanuel.hotelavailapi.output.adapter;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.output.mapper.KafkaSearchMessageMapper;
import com.arnoldmanuel.hotelavailapi.output.model.KafkaSearchMessage;
import com.arnoldmanuel.hotelavailapi.application.output.SearchMessagingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaSearchMessagingAdapter implements SearchMessagingPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaSearchMessagingAdapter.class);

    private final ReactiveKafkaProducerTemplate<String, KafkaSearchMessage> kafkaTemplate;
    private final KafkaSearchMessageMapper kafkaSearchMessageMapper;
    private static final String TOPIC = "hotel_availability_searches";

    public KafkaSearchMessagingAdapter(ReactiveKafkaProducerTemplate<String, KafkaSearchMessage> kafkaTemplate,
            KafkaSearchMessageMapper kafkaSearchMessageMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaSearchMessageMapper = kafkaSearchMessageMapper;
    }

    @Cacheable(value = "searches", key = "#search.searchId().value()")
    @Override
    public Mono<SearchId> send(SearchDomain search) {
        return Mono.just(search)
                .map(kafkaSearchMessageMapper::map)
                .flatMap(kafkaSearchMessage ->
                        kafkaTemplate.send(TOPIC, String.valueOf(search.searchId().value()), kafkaSearchMessage)
                                .thenReturn(search.searchId())
                )
                .retry(3)
                .doOnError(e -> log.error("Error sending message to Kafka: {}", e.getMessage()))
                .onErrorResume(e -> Mono.just(new SearchId("0")));
    }
}
