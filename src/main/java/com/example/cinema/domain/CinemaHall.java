package com.example.cinema.domain;

import com.example.cinema.core.SeatType;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "cinema_hall")
public class CinemaHall extends AuditableEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "seats_amount", nullable = false)
    private Short seatsAmount;
    @Type(type = "jsonb")
    @Column(name = "seats_type", nullable = false, columnDefinition = "jsonb")
    private EnumMap<SeatType, HashSet<Integer>> seatsType;

    public CinemaHall() {
    }

    public CinemaHall(Long id, String name, Short seatsAmount) {
        super(id);
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
        this.seatsAmount = seatsAmount;
    }

    public EnumMap<SeatType, HashSet<Integer>> getSeatsType() {
        if (seatsType == null) {
            seatsType = new EnumMap<>(SeatType.class);
        }
        return seatsType;
    }

    /**
     * Replaces old {@link #seatsType} with new one.
     * {@link #seatsAmount} must be not null
     *
     * @param seatsType new one
     * @throws IllegalArgumentException if incoming amount of seats does not match {@link #seatsAmount} or they have duplicated seats numbers
     * @throws NullPointerException     if you try to set new {@link #seatsType} when {@link #seatsAmount} not set yet
     */
    public void setSeatsType(EnumMap<SeatType, HashSet<Integer>> seatsType) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(seatsAmount, "Seats amount must be not null");
        validateSeats(seatsType.values());
        this.seatsType = seatsType;
    }

    private void validateSeats(Collection<HashSet<Integer>> values) throws IllegalArgumentException {
        final HashSet<Integer> hashSet = new HashSet<>(40);
        values.forEach(hashSet::addAll);
        int count = 0;
        for (HashSet<Integer> number :
                values) {
            count += number.size();
        }
        if (hashSet.size() < count) {
            throw new IllegalArgumentException("Seats type contains duplicate seats numbers");
        }
        if (count != seatsAmount) {
            throw new IllegalArgumentException(" Amount of seats transferred to set seats type, do not match the total amount of seats for current cinema hall.");
        }

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
