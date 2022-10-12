package com.example.cinema.dao;

import com.example.cinema.core.FilterMarker;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Query filter to find required timetable.
 *
 * @author Alexandr Yefremov
 */
@JsonDeserialize(builder = TimeTableQueryFilter.Builder.class)
public final class TimeTableQueryFilter implements Serializable, FilterMarker {
    private final Integer page;
    private final Integer limit;
    private final LocalDate dateSession;
    private final Long movieId;
    /**
     * Specify case when only timetables with free seats will be selected
     */
    private final boolean hasFreeSeats;

    private TimeTableQueryFilter(Builder builder) {
        this.page = builder.page == null || builder.page < 0 ? 0 : builder.page;
        this.limit = builder.limit == null || builder.limit < 0 ? 10 : builder.limit;
        this.dateSession = builder.dateSession;
        this.movieId = builder.movieId;
        this.hasFreeSeats = builder.hasFreeSeats;
    }

    public static TimeTableQueryFilter.Builder builder() {
        return new TimeTableQueryFilter.Builder();
    }

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public Optional<LocalDate> getDateSession() {
        return ofNullable(dateSession);
    }

    public Optional<Long> getMovieId() {
        return ofNullable(movieId);
    }

    public Boolean isHasFreeSeats() {
        return hasFreeSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTableQueryFilter)) return false;
        TimeTableQueryFilter that = (TimeTableQueryFilter) o;
        return isHasFreeSeats() == that.isHasFreeSeats() && Objects.equals(getPage(), that.getPage()) && Objects.equals(getLimit(), that.getLimit()) && Objects.equals(getDateSession(), that.getDateSession()) && Objects.equals(getMovieId(), that.getMovieId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPage(), getLimit(), getDateSession(), getMovieId(), isHasFreeSeats());
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private Integer page;
        private Integer limit;
        private LocalDate dateSession;
        private Long movieId;
        private boolean hasFreeSeats;

        public TimeTableQueryFilter.Builder page(final Integer page) {
            this.page = page;
            return this;
        }

        public TimeTableQueryFilter.Builder limit(final Integer limit) {
            this.limit = limit;
            return this;
        }

        public TimeTableQueryFilter.Builder dateSession(final LocalDate dateTimeSession) {
            this.dateSession = dateTimeSession;
            return this;
        }

        public TimeTableQueryFilter.Builder movieId(final Long movieId) {
            this.movieId = movieId;
            return this;
        }

        public TimeTableQueryFilter.Builder hasFreeSeats(boolean hasFreeSeats) {
            this.hasFreeSeats = hasFreeSeats;
            return this;
        }

        public TimeTableQueryFilter build() {
            return new TimeTableQueryFilter(this);
        }
    }
}
