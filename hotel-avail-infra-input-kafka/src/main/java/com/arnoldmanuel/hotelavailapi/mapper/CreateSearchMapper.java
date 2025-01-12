package com.arnoldmanuel.hotelavailapi.mapper;

import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.model.CreateSearchMessage;

@Mapper(componentModel = "spring")
public interface CreateSearchMapper {

    @Mapping(target = "searchId", ignore = true)
    @Mapping(target = "search.hotel.hotelId", source = "hotelId")
    @Mapping(target = "search.dateRange.checkIn", source = "checkIn")
    @Mapping(target = "search.dateRange.checkOut", source = "checkOut")
    @Mapping(target = "search.ages", source = "ages")
    SearchDomain toDomain(CreateSearchMessage message);
}
