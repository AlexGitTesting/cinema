package com.example.cinema.domain;

import com.example.cinema.core.SeatType;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.cinema.core.ValidatorHelper.validateShort;
import static java.util.Collections.unmodifiableMap;

/**
 * Entity that represents cinema hall.
 *
 * @author Alexandr Yefremov
 */
@Entity
@Table(name = "cinema_hall")
public class CinemaHall extends AuditableEntity {
    {
        seatsType = new EnumMap<>(SeatType.class);
    }

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "seats_amount", nullable = false)
    private Short seatsAmount;
    @Type(type = "jsonb")
    @Column(name = "seats_type", nullable = false, columnDefinition = "jsonb")
    private Map<SeatType, HashSet<Short>> seatsType;

    public CinemaHall() {
    }

    public CinemaHall(Long id) {
        super(id);
    }

    public CinemaHall(Long id, String name, Short seatsAmount) {
        super(id);
        validateSeatsAmount(seatsAmount);
        this.name = name;
        this.seatsAmount = seatsAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getSeatsAmount() {
        return seatsAmount;
    }

    public void setSeatsAmount(Short seatsAmount) {
        validateSeatsAmount(seatsAmount);
        this.seatsAmount = seatsAmount;
    }

    /**
     * Validate seats amount.
     *
     * @param seatsAmount amount of a seats
     * @throws IllegalArgumentException if amount is not correct
     */
    private void validateSeatsAmount(Short seatsAmount) throws IllegalArgumentException {
        if (seatsAmount == null || seatsAmount < 1) throw new IllegalArgumentException("seats.amount.less.one");
    }

    /**
     * Returns unmodifiable map of the seat types. To add seats types use {@link CinemaHall#setSeatsType(Map)}
     *
     * @return unmodifiable collection
     * @throws UnsupportedOperationException when try modify seats types
     */
    public Map<SeatType, HashSet<Short>> getSeatsType() throws UnsupportedOperationException {
        return unmodifiableMap(seatsType);
    }

    /**
     * Replaces old {@link #seatsType} with new one but does not add.
     * {@link #seatsAmount} must be not null
     *
     * @param seatsType new one
     * @throws IllegalArgumentException see {@link CinemaHall#validateSeats(Collection)}
     * @throws NullPointerException     if you try to set new {@link #seatsType} when {@link #seatsAmount} not set yet
     */
    public void setSeatsType(Map<SeatType, HashSet<Short>> seatsType) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(seatsAmount, "seats.must.not.empty");
        if (seatsType.isEmpty()) {
            throw new IllegalArgumentException("seats.must.not.empty");
        }
        validateSeats(seatsType.values());
        this.seatsType = seatsType;
    }

    /**
     * Validate new seats before they will be added.
     *
     * @param values seat numbers
     * @throws IllegalArgumentException if incoming amount of seats does not match {@link #seatsAmount} or they have duplicated seats numbers
     */
    private void validateSeats(Collection<HashSet<Short>> values) throws IllegalArgumentException {
        final long countDist = values.stream()
                .flatMap(Collection::stream)
                .distinct()
                .count();
        final long countWithoutDist = values.stream()
                .mapToLong(Collection::size)
                .sum();
        if (countDist != countWithoutDist) {
            throw new IllegalArgumentException("seats.contain.duplicates");
        }
        if (countDist != seatsAmount) {
            throw new IllegalArgumentException("seats.not.match.total.amount");
        }
    }

    /**
     * Checks, if the candidates (numbers of seats) are within the range of the current cinema hall's seats.
     *
     * @param candidates numbers of seats
     * @return true if the current cinema hall contains all such numbers (seats), otherwise false
     * @throws IllegalArgumentException if candidates null or empty
     */
    public boolean areSeatsRelatedToCurrentHall(Set<Short> candidates) throws IllegalArgumentException {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException(" field.error.empty.collection");
        }
        return candidates.stream()
                .allMatch(candidate -> seatsType.values().stream().anyMatch(group -> group.contains(candidate)));
    }


    /**
     * Returns {@link SeatType} by seat number.
     *
     * @param seatNumber seat number
     * @return {@link SeatType}
     * @throws IllegalStateException    if cinema hall contains duplicated seats or if seat type by number is not found
     * @throws IllegalArgumentException if argument null or less then 1
     */
    public SeatType getSeatTypeBySeatNumber(Short seatNumber) throws IllegalStateException, IllegalArgumentException {
        validateShort(seatNumber);
        final Set<SeatType> collect = getSeatsType().keySet()
                .stream()
                .filter(current -> getSeatsType().get(current).contains(seatNumber))
                .collect(Collectors.toUnmodifiableSet());
        if (collect.size() > 1) {
            throw new IllegalStateException("seats.contain.duplicates");
        }
        return collect.stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Seat type by  number not found"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CinemaHall that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getSeatsAmount(), that.getSeatsAmount()) && Objects.equals(getSeatsType(), that.getSeatsType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getSeatsAmount(), getSeatsType());
    }

    @Override
    public String toString() {
        return "CinemaHall{" +
                "name='" + name + '\'' +
                ", seatsAmount=" + seatsAmount +
                ", seatsType=" + seatsType +
                '}';
    }
}
