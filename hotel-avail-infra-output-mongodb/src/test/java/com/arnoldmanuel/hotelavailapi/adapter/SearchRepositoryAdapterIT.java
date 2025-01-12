package com.arnoldmanuel.hotelavailapi.adapter;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arnoldmanuel.hotelavailapi.application.output.SearchRepositoryPort;
import com.arnoldmanuel.hotelavailapi.config.MongoConfig;
import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.entity.SearchEntity;
import com.arnoldmanuel.hotelavailapi.mapper.SearchEntityMapperImpl;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import reactor.test.StepVerifier;

@SpringBootTest(classes = {
    EmbeddedMongoAutoConfiguration.class,
    SearchRepositoryAdapter.class,
    SearchEntityMapperImpl.class,
    MongoConfig.class})
public class SearchRepositoryAdapterIT {

    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    @Autowired
    SearchRepositoryPort searchRepositoryAdapter;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(SearchEntity.class).block();
    }

    @Test
    void saveSearchDomain_withValidSearchDomain_thenSearchDomainIsSaved() {
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        );
        SearchDomain searchDomain = new SearchDomain(null, searchAvailabilityDomain);
        Query query = new Query(Criteria.where("searchId").is(searchDomain.searchId().value()));

        StepVerifier.create(searchRepositoryAdapter.save(searchDomain))
                .expectComplete()
                .verify();

        StepVerifier.create(mongoTemplate.findOne(query, SearchEntity.class))
                .assertNext(searchEntity -> {
                    assertNotNull(searchEntity);
                    assertEquals(searchDomain.searchId().value(), searchEntity.getSearchId());
                    assertEquals(searchDomain.search().dateRange().checkIn(), searchEntity.getCheckIn());
                    assertEquals(searchDomain.search().dateRange().checkOut(), searchEntity.getCheckOut());
                    assertEquals(searchDomain.search().ages(), searchEntity.getAges());
                    assertEquals(1, searchEntity.getCount());
                })
                .expectComplete().verify();
    }

    @Test
    void saveSearchDomainTwice_withSameProperties_thenCountIsUpdated() {
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        );
        SearchDomain searchDomain = new SearchDomain(null, searchAvailabilityDomain);
        Query query = new Query(Criteria.where("searchId").is(searchDomain.searchId().value()));

        StepVerifier.create(searchRepositoryAdapter.save(searchDomain))
                .verifyComplete();

        StepVerifier.create(searchRepositoryAdapter.save(searchDomain))
                .verifyComplete();

        StepVerifier.create(mongoTemplate.findOne(query, SearchEntity.class))
                .assertNext(searchEntity -> {
                    assertNotNull(searchEntity);
                    assertEquals(searchDomain.searchId().value(), searchEntity.getSearchId());
                    assertEquals(searchDomain.search().dateRange().checkIn(), searchEntity.getCheckIn());
                    assertEquals(searchDomain.search().dateRange().checkOut(), searchEntity.getCheckOut());
                    assertEquals(searchDomain.search().ages(), searchEntity.getAges());
                    assertEquals(2, searchEntity.getCount());
                })
                .verifyComplete();
    }

    @Test
    void countSearch_withValidSearchId_thenReturnSimilarSearchDomain() {
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        );
        SearchDomain searchDomain = new SearchDomain(null, searchAvailabilityDomain);
        SearchId searchId = searchDomain.searchId();
        searchRepositoryAdapter.save(searchDomain).block();
        searchRepositoryAdapter.save(searchDomain).block();
        searchRepositoryAdapter.save(searchDomain).block();

        StepVerifier.create(searchRepositoryAdapter.countSearch(searchId))
                .assertNext(similarSearchDomain -> {
                    assertNotNull(similarSearchDomain);
                    assertEquals(searchId.value(), similarSearchDomain.searchId().value());
                    assertEquals(3L, similarSearchDomain.count());
                })
                .verifyComplete();
    }
}
