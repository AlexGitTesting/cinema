package com.example.cinema.domain;

import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class Order extends AuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "time_table_id", nullable = false, updatable = false)
    private TimeTable timeTable;
    @JoinColumn(name = "order_price", nullable = false)
    private Integer orderPrice;
    @Type(type = "jsonb")
    @Column(name = "seats", columnDefinition = "jsonb", nullable = false)
    private HashSet<Short> seats;
    @Column(name = "customer")
    private String customer;

    {
        orderPrice = 0;
        seats = new HashSet<>();
    }

    public Order() {
    }

    public Order(Long id, @NonNull TimeTable timeTable, @NonNull Integer orderPrice, String customer) {
        super(id);
        this.timeTable = timeTable;
        this.orderPrice = orderPrice;
        this.customer = customer;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(@NonNull TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(@NonNull Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public HashSet<Short> getSeats() {
        return seats;
    }

    public void setSeats(@NonNull HashSet<Short> seats) {
        this.seats.addAll(seats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        if (!super.equals(o)) return false;
        return getTimeTable().equals(order.getTimeTable()) && getOrderPrice().equals(order.getOrderPrice()) && getSeats().equals(order.getSeats()) && Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTimeTable(), getOrderPrice(), getSeats(), customer);
    }

    @Override
    public String toString() {
        return "Order{" +
                "timeTable=" + timeTable +
                ", orderPrice=" + orderPrice +
                ", seats=" + seats +
                ", customer='" + customer + '\'' +
                '}';
    }
}
