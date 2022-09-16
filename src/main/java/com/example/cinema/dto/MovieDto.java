package com.example.cinema.dto;

import com.example.cinema.core.RequiredFieldsForCreation;
import com.example.cinema.core.RequiredFieldsForUpdating;
import com.example.cinema.domain.Movie;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Represent DTO of {@link Movie}.
 *
 * @author Alexandr Yefremov
 */
@JsonDeserialize(builder = MovieDto.Builder.class)
public class MovieDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 42L;
    @Null(groups = RequiredFieldsForCreation.class, message = "field.error.null")
    @NotNull(groups = RequiredFieldsForUpdating.class, message = "field.error.not.null")
    @Min(value = 1, groups = RequiredFieldsForUpdating.class, message = "field.error.min._1")
    private final Long id;
    @NotBlank(message = "field.error.blank")
    @Size(max = 20, message = "field.error.size.incorrect")
    private final String title;
    @NotNull(message = "field.error.not.null")
    @Min(value = 1, message = "field.error.min._1")
    private final Short timing;
    @NotBlank(message = "field.error.blank")
    @Size(max = 20, message = "field.error.size.incorrect")
    private final String producer;

    private MovieDto(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.timing = builder.timing;
        this.producer = builder.producer;
    }


    public static Builder builder() {
        return new Builder();
    }

    public Optional<Long> getId() {
        return ofNullable(id);
    }

    public Optional<String> getTitle() {
        return ofNullable(title);
    }

    public Optional<Short> getTiming() {
        return ofNullable(timing);
    }

    public Optional<String> getProducer() {
        return ofNullable(producer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieDto movieDto)) return false;
        return Objects.equals(getId(), movieDto.getId()) && Objects.equals(getTitle(), movieDto.getTitle()) && Objects.equals(getTiming(), movieDto.getTiming()) && Objects.equals(getProducer(), movieDto.getProducer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getTiming(), getProducer());
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", timing=" + timing +
                ", producer='" + producer + '\'' +
                '}';
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private Long id;
        private String title;
        private Short timing;
        private String producer;

        public MovieDto build() {
            return new MovieDto(this);
        }

        public MovieDto.Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public MovieDto.Builder title(final String title) {
            this.title = title;
            return this;
        }

        public MovieDto.Builder timing(final Short timing) {
            this.timing = timing;
            return this;
        }

        public MovieDto.Builder producer(final String producer) {
            this.producer = producer;
            return this;
        }
    }
}
