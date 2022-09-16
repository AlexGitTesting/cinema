package com.example.cinema.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity that represents movie table.
 *
 * @author Alexandr Yefremov
 */
@Entity
@Table(name = "movie")
public class Movie extends AuditableEntity {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "timing", nullable = false)
    private Short timing;
    @Column(name = "producer", nullable = false)
    private String producer;

    public Movie() {
    }

    public Movie(Long id) {
        super(id);
    }

    public Movie(Long id, String title, Short timing, String producer) {
        super(id);
        this.title = title;
        this.timing = timing;
        this.producer = producer;
    }

    public Movie(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String title, Short timing, String producer) {
        super(id, createdDate, modifiedDate);
        this.title = title;
        this.timing = timing;
        this.producer = producer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getTiming() {
        return timing;
    }

    public void setTiming(Short timing) {
        this.timing = timing;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return getTitle().equals(movie.getTitle()) && getTiming().equals(movie.getTiming()) && getProducer().equals(movie.getProducer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTitle(), getTiming(), getProducer());
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + getId() + '\'' +
                "title='" + title + '\'' +
                ", timing=" + timing +
                ", producer='" + producer + '\'' +
                '}';
    }
}
