package com.example.cinema.domain;

import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.util.Collections.unmodifiableSet;

/**
 * Entity for time table.
 *
 * @author Alexandr Yefremov
 */
@Entity
@Table(name = "time_table")
public class TimeTable extends AuditableEntity {
    {
        closedSeats = new HashSet<>();
        isSold = FALSE;
        basePrice = (short) 0;
    }

    public static final String BOOKED_SEATS_OUT_OF_RANGE = "Booked seats are out of the range of the current cinema hall";
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
    @Column(name = "closed_seats", columnDefinition = "jsonb", nullable = false)
    private final Set<Short> closedSeats;
    @Column(name = "sold", nullable = false)
    private Boolean isSold;

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

    /**
     * Sets start session.
     *
     * @param startSession when movie will start.
     * @throws IllegalArgumentException if argument earlier then {@link LocalDateTime#now()}
     */
    public void setStartSession(@NonNull LocalDateTime startSession) throws IllegalArgumentException {
        if (startSession.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("start.session.not.correct");
        this.startSession = startSession;
    }

    public Short getBasePrice() {
        return basePrice;
    }

    /**
     * Sets base price for the ticket
     *
     * @param basePrice price
     * @throws IllegalArgumentException if price less then zero
     */
    public void setBasePrice(@NonNull Short basePrice) throws IllegalArgumentException {
        if (basePrice < 0) {
            throw new IllegalArgumentException("base.price.incorrect");
        }
        this.basePrice = basePrice;
    }

    public Boolean getIsSold() {
        return isSold;
    }

    public void setIsSold(Boolean sold) {
        isSold = sold;
    }

    /**
     * Return copy as unmodifiable set for reading only.
     * To add new seats use {@link #addClosedSeats(Set)}
     *
     * @return unmodifiable set
     * @throws UnsupportedOperationException if you try modify set
     */
    public Set<Short> getClosedSeats() throws UnsupportedOperationException {
        return unmodifiableSet(closedSeats);
    }

    /**
     * Adds booked seats to the existed and does some additional checks.
     * Set isClosed to true if all seats are booked.
     *
     * @param arg will be added
     * @return true if everything went well
     * @throws IllegalArgumentException if incoming collection is empty
     *                                  or seat numbers are already booked,
     *                                  or total amount of seats is greater then {@link CinemaHall#getSeatsAmount()},
     *                                  or seat number is out of the range of the Cinema hall's seat numbers
     */
    public boolean addClosedSeats(@NonNull Set<Short> arg) throws IllegalArgumentException {
        if (arg.isEmpty()) {
            throw new IllegalArgumentException("Booked seats are empty");
        }
        if (closedSeats.size() + arg.size() > getCinemaHall().getSeatsAmount().intValue()) {
            throw new IllegalArgumentException("Amount of the booked seats are greater then total amount of seats of the current cinema hall.");
        }
        if (arg.stream().anyMatch(closedSeats::contains)) {
            throw new IllegalArgumentException("seats.already.closed");
        }
        if (!cinemaHall.areSeatsRelatedToCurrentHall(arg)) {

            throw new IllegalArgumentException(BOOKED_SEATS_OUT_OF_RANGE);
        }
        closedSeats.addAll(arg);
        if (closedSeats.size() == cinemaHall.getSeatsAmount().intValue()) {
            this.setIsSold(true);
        }
        return true;
    }

    /**
     * Removes incoming seats from booked seats.
     *
     * @param seats seats
     * @throws IllegalArgumentException if reserved seats do not contain all seats for cancelling
     *                                  or argument is empty.
     */
    public void reopenClosedSeats(@NonNull Set<Short> seats) throws IllegalArgumentException {
        if (seats.isEmpty()) {
            throw new IllegalArgumentException("Seats for cancelling booking are empty");
        }
        if (!closedSeats.containsAll(seats)) {
            throw new IllegalArgumentException("Seats for cancel booking are not contained in the current timetable");
        }
        seats.forEach(closedSeats::remove);
        setIsSold(FALSE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTable)) return false;
        if (!super.equals(o)) return false;
        TimeTable timeTable = (TimeTable) o;
        return getMovie().getId().equals(timeTable.getMovie().getId())
                && getCinemaHall().getId().equals(timeTable.getCinemaHall().getId())
                && Objects.equals(getStartSession(), timeTable.getStartSession())
                && Objects.equals(getBasePrice(), timeTable.getBasePrice())
                && Objects.equals(getClosedSeats(), timeTable.getClosedSeats())
                && getIsSold().equals(timeTable.getIsSold());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMovie().getId(), getCinemaHall().getId(), getStartSession(), getBasePrice(), getClosedSeats(), getIsSold());
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "movie id =" + movie.getId() +
                ", cinemaHall id =" + cinemaHall.getId() +
                ", startSession=" + startSession +
                ", basePrice=" + basePrice +
                ", closedSeats=" + closedSeats +
                ", isSold=" + isSold +
                '}';
    }
}
