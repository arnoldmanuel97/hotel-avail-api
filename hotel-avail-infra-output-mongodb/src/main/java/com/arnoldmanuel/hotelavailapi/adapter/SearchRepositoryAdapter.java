package com.arnoldmanuel.hotelavailapi.adapter;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.application.output.SearchRepositoryPort;

import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.domain.SimilarSearchDomain;
import com.arnoldmanuel.hotelavailapi.entity.SearchEntity;
import com.arnoldmanuel.hotelavailapi.mapper.SearchEntityMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SearchRepositoryAdapter implements SearchRepositoryPort {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final SearchEntityMapper searchEntityMapper;

    public SearchRepositoryAdapter(ReactiveMongoTemplate reactiveMongoTemplate,
                                   SearchEntityMapper searchEntityMapper) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.searchEntityMapper = searchEntityMapper;
    }

    @Override
    public Mono<Void> save(SearchDomain searchDomain) {
        return Mono.defer(() -> {
            Query query = new Query(Criteria.where("searchId").is(searchDomain.searchId().value()));

            Update update = new Update()
                    .setOnInsert("hotelId", searchDomain.search().hotel().hotelId())
                    .setOnInsert("checkIn", searchDomain.search().dateRange().checkIn())
                    .setOnInsert("checkOut", searchDomain.search().dateRange().checkOut())
                    .setOnInsert("ages", searchDomain.search().ages())
                    .inc("count", 1);
            return reactiveMongoTemplate.upsert(query, update, SearchEntity.class).thenEmpty(Mono.empty());
        });
    }

    @Override
    public Mono<SimilarSearchDomain> countSearch(SearchId searchId) {
        return reactiveMongoTemplate.findOne(
                Query.query(Criteria.where("searchId").is(searchId.value())), SearchEntity.class)
                .map(searchEntityMapper::toDomain);
    }


}
