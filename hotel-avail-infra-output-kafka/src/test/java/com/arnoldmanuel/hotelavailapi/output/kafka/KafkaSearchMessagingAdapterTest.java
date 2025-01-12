package com.arnoldmanuel.hotelavailapi.output.kafka;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.output.adapter.KafkaSearchMessagingAdapter;
import com.arnoldmanuel.hotelavailapi.output.config.KafkaConfig;
import com.arnoldmanuel.hotelavailapi.output.mapper.KafkaSearchMessageMapperImpl;
import com.arnoldmanuel.hotelavailapi.output.model.KafkaSearchMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EmbeddedKafka(partitions = 1,
        topics = {"hotel_availability_searches"},
        ports = {9092})
@EnableAutoConfiguration
@Import({TestChannelBinderConfiguration.class})
@SpringBootTest(classes = {
    KafkaSearchMessagingAdapter.class,
    KafkaConfig.class,
    KafkaSearchMessageMapperImpl.class})
class KafkaSearchMessagingAdapterTest {

    @Autowired
    OutputDestination outputDestination;

    @Autowired
    KafkaSearchMessagingAdapter kafkaSearchMessagingAdapter;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;
    Consumer<String, KafkaSearchMessage> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true",
                embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        JsonDeserializer<KafkaSearchMessage> jsonDeserializer = new JsonDeserializer<>(KafkaSearchMessage.class);
        jsonDeserializer.addTrustedPackages("*");
        ConsumerFactory<String, KafkaSearchMessage> cf = new DefaultKafkaConsumerFactory<>(consumerProps,
                new StringDeserializer(), jsonDeserializer);
        consumer = cf.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "hotel_availability_searches");
    }

    @Test
    void whenSendSearchMessage_withValidSearchDomain_thenSendToKafka() {
        SearchAvailabilityDomain searchAvailabilityDomain = new SearchAvailabilityDomain(
                new SearchAvailabilityDomain.Hotel("hotelId"),
                new SearchAvailabilityDomain.DateRange(LocalDate.now(), LocalDate.now().plusDays(4)),
                List.of(7, 23)
        );
        SearchDomain searchDomain = new SearchDomain(
                new SearchId("123"),
                searchAvailabilityDomain
        );

        kafkaSearchMessagingAdapter.send(searchDomain).block();

        ConsumerRecord<String, KafkaSearchMessage> record = KafkaTestUtils.getSingleRecord(consumer, "hotel_availability_searches");

        assertNotNull(record);
        KafkaSearchMessage payload = record.value();

        assertEquals(searchDomain.searchId().value(), payload.searchId());
        assertEquals(searchAvailabilityDomain.hotel().hotelId(), payload.hotelId());
        assertEquals(searchAvailabilityDomain.dateRange().checkIn(), payload.checkIn());
        assertEquals(searchAvailabilityDomain.dateRange().checkOut(), payload.checkOut());
        assertEquals(searchAvailabilityDomain.ages(), payload.ages());
    }
}
