package com.example.cinema.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@JsonDeserialize(builder = MovieQueryFilter.Builder.class)
public class MovieQueryFilter implements Serializable {
    private final Integer page;
    private final Integer limit;
    private final boolean sortingAscending;
    private final String title;
    private final String producer;
    /**
     * false- all movies, true the session will be started from the current time
     */
    private final boolean isActive;

    private MovieQueryFilter(Builder builder) {
        this.page = builder.page == null || builder.page < 0 ? 0 : builder.page;
        this.limit = builder.limit == null || builder.limit < 0 ? 2 : builder.limit;
        this.sortingAscending = builder.sortingAscending;
        this.title = builder.title;
        this.producer = builder.producer;
        this.isActive = builder.notStarted;
    }


    public static MovieQueryFilter.Builder builder() {
        return new MovieQueryFilter.Builder();
    }

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean isSortingAscending() {
        return sortingAscending;
    }

    public Optional<String> getTitle() {
        return ofNullable(title);
    }

    public Optional<String> getProducer() {
        return ofNullable(producer);
    }

    public boolean isActive() {
        return isActive;
    }

    // TODO: 11.09.2022 equals & hash
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieQueryFilter)) return false;
        MovieQueryFilter that = (MovieQueryFilter) o;
        return isSortingAscending() == that.isSortingAscending() && isActive() == that.isActive() && Objects.equals(getPage(), that.getPage()) && Objects.equals(getLimit(), that.getLimit()) && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getProducer(), that.getProducer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPage(), getLimit(), isSortingAscending(), getTitle(), getProducer(), isActive());
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private Integer page;
        private Integer limit;
        private boolean sortingAscending;
        private String title;
        private String producer;
        private boolean notStarted;

        public MovieQueryFilter.Builder page(final Integer page) {
            this.page = page;
            return this;
        }

        public MovieQueryFilter.Builder limit(final Integer limit) {
            this.limit = limit;
            return this;
        }

        public MovieQueryFilter.Builder sortingAscending(final boolean sortingAscending) {
            this.sortingAscending = sortingAscending;
            return this;
        }

        public MovieQueryFilter.Builder title(final String title) {
            this.title = title;
            return this;
        }

        public MovieQueryFilter.Builder producer(final String producer) {
            this.producer = producer;
            return this;
        }

        public MovieQueryFilter.Builder notStarted(final boolean notStarted) {
            this.notStarted = notStarted;
            return this;
        }

        public MovieQueryFilter build() {
            return new MovieQueryFilter(this);
        }

    }
}
