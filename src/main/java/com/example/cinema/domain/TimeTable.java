package com.example.cinema.domain;

import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "time_table")
public class TimeTable extends AuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    private Movie movie;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_hall_id", nullable = false, updatable = false)
    private CinemaHall cinemaHall;
    @Column(name = "start_session", nullable = false)
    private LocalDateTime startSession;
    @Column(name = "base_price", nullable = false)
    private Short basePrice;
    @Type(type = "jsonb")
    @Column(name = "closed_seats", columnDefinition = "jsonb")
    private HashSet<Short> closedSeats;
    @Column(name = "sold", nullable = false)
    private Boolean isSold;

    {
        closedSeats = new HashSet<>();
        isSold = Boolean.FALSE;
        basePrice = (short) 0;
    }


    public TimeTable() {
    }

    public TimeTable(Long id) {
        super(id);
    }

    public TimeTable(Long id, @NonNull Movie movie, @NonNull CinemaHall cinemaHall, @NonNull LocalDateTime startSession, @NonNull Short basePrice, @NonNull Boolean isSold) {
        super(id);
        this.movie = movie;
        this.cinemaHall = cinemaHall;
        this.startSession = startSession;
        this.basePrice = basePrice;
        this.isSold = isSold;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(@NonNull Movie movie) {
        this.movie = movie;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }

    public void setCinemaHall(@NonNull CinemaHall cinemaHall) {
        this.cinemaHall = cinemaHall;
    }

    public LocalDateTime getStartSession() {
        return startSession;
    }

    public void setStartSession(@NonNull LocalDateTime startSession) {
        this.startSession = startSession;
    }

    public Short getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(@NonNull Short basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("seats.already.closed");
        }
        this.basePrice = basePrice;
    }

    public Boolean getSold() {
        return isSold;
    }

    public void setSold(Boolean sold) {
        isSold = sold;
    }

    public HashSet<Short> getClosedSeats() {
        return closedSeats;
    }

    /**
     * Adds closed seats to the existed, reduces duplicating the same seats numbers
     *
     * @param arg will be added
     * @return if everything went well
     * @throws IllegalArgumentException if some seat is already closed
     */
    public boolean addClosedSeats(@NonNull HashSet<Short> arg) throws IllegalArgumentException {
        // TODO: 10.09.2022  remove
//        if (!closedSeats.isEmpty()) {
//            arg.forEach(seat -> {
//                if (closedSeats.contains(seat)) {
//                    throw new IllegalArgumentException("seats.already.closed");
//                }
//            });
        if (arg.stream().anyMatch(seat -> closedSeats.contains(seat))) {
            throw new IllegalArgumentException("seats.already.closed");
        }
        return closedSeats.addAll(arg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTable timeTable)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getMovie(), timeTable.getMovie()) && Objects.equals(getCinemaHall(), timeTable.getCinemaHall()) && Objects.equals(getStartSession(), timeTable.getStartSession()) && getBasePrice().equals(timeTable.getBasePrice()) && getClosedSeats().equals(timeTable.getClosedSeats()) && isSold.equals(timeTable.isSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMovie(), getCinemaHall(), getStartSession(), getBasePrice(), getClosedSeats(), isSold);
    }
}
