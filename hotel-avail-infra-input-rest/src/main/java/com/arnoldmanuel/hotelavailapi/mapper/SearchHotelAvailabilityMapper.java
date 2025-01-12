package com.arnoldmanuel.hotelavailapi.mapper;

import com.arnoldmanuel.hotelavailapi.domain.SearchAvailabilityDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchDomain;
import com.arnoldmanuel.hotelavailapi.domain.SearchId;
import com.arnoldmanuel.hotelavailapi.domain.SimilarSearchDomain;
import com.arnoldmanuel.hotelavailapi.model.CountSearchResponse;
import com.arnoldmanuel.hotelavailapi.model.SearchHotelAvailabilityRequest;
import com.arnoldmanuel.hotelavailapi.model.SearchHotelAvailabilityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SearchHotelAvailabilityMapper {

    @Mapping(target = "searchId", ignore = true)
    @Mapping(target = "search.hotel.hotelId", source = "hotelId")
    @Mapping(target = "search.dateRange.checkIn", source = "checkIn")
    @Mapping(target = "search.dateRange.checkOut", source = "checkOut")
    @Mapping(target = "search.ages", source = "ages")
    SearchDomain toDomain(SearchHotelAvailabilityRequest request);

    @Mapping(target = "searchId", source = "value")
    SearchHotelAvailabilityResponse fromDomain(SearchId searchId);

    @Mapping(target = "value", source = "searchId")
    SearchId toDomain(String searchId);

    @Mapping(target = "searchId", source = "searchId.value")
    @Mapping(target = "search.hotelId", source = "search.hotel.hotelId")
    @Mapping(target = "search.checkIn", source = "search.dateRange.checkIn")
    @Mapping(target = "search.checkOut", source = "search.dateRange.checkOut")
    @Mapping(target = "search.ages", source = "search.ages")
    @Mapping(target = "count", source = "count")
    CountSearchResponse fromDomain(SimilarSearchDomain similarSearchDomain);
    
}
