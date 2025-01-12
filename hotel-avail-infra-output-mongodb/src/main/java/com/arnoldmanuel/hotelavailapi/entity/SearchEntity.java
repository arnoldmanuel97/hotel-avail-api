package com.arnoldmanuel.hotelavailapi.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "searchs")
public class SearchEntity {

    @Id
    private String id;
    private String searchId;
    private String hotelId;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate checkIn;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate checkOut;
    private List<Integer> ages;

    private long count;

    public void setId(String id) {
        this.id = id;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public void setAges(List<Integer> ages) {
        this.ages = ages;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public String getSearchId() {
        return searchId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public long getCount() {
        return count;
    }

}
