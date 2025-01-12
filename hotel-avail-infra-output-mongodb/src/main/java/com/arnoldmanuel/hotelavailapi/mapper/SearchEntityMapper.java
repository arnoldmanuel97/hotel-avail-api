package com.arnoldmanuel.hotelavailapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arnoldmanuel.hotelavailapi.domain.SimilarSearchDomain;
import com.arnoldmanuel.hotelavailapi.entity.SearchEntity;

@Mapper(componentModel = "spring")
public interface SearchEntityMapper {

    @Mapping(target = "searchId.value", source = "searchId")
    @Mapping(target = "search.hotel.hotelId", source = "hotelId")
    @Mapping(target = "search.dateRange.checkIn", source = "checkIn")
    @Mapping(target = "search.dateRange.checkOut", source = "checkOut")
    @Mapping(target = "search.ages", source = "ages")
    @Mapping(target = "count", source = "count")
    SimilarSearchDomain toDomain(SearchEntity searchEntity);
}
