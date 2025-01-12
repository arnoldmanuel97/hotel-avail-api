package com.arnoldmanuel.hotelavailapi.domain;

public record SearchDomain(
        SearchId searchId,
        SearchAvailabilityDomain search
) {

    public SearchDomain {
        searchId = searchId == null ? SearchId.generateFrom(search) : searchId;
    }
}
