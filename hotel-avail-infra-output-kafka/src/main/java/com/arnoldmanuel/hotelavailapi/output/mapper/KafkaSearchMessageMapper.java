package com.arnoldmanuel.hotelavailapi.output.mapper;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.output.model.KafkaSearchMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KafkaSearchMessageMapper {

    @Mapping(target = "searchId", source = "searchId.value")
    @Mapping(target = "hotelId", source = "search.hotel.hotelId")
    @Mapping(target = "checkIn", source = "search.dateRange.checkIn")
    @Mapping(target = "checkOut", source = "search.dateRange.checkOut")
    @Mapping(target = "ages", source = "search.ages")
    KafkaSearchMessage map(SearchDomain searchDomain);
}
