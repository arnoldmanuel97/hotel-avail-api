package com.arnoldmanuel.hotelavailapi.domain;

import com.arnoldmanuel.hotelavailapi.exceptions.DomainException;

public record SimilarSearchDomain (
        SearchId searchId,
        SearchAvailabilityDomain search,
        Long count
){
    public SimilarSearchDomain {
        if (searchId == null) {
            throw new DomainException("searchId cannot be null");
        }
    }
}
