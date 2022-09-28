package com.example.cinema.dto;

import com.example.cinema.core.DtoMarker;
import com.example.cinema.domain.OrderTable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * Represents DTO of {@link OrderTable}.
 *
 * @author Alexandr Yefremov
 */
@JsonDeserialize(builder = OrderDto.Builder.class)
public class OrderDto implements Serializable, DtoMarker {
    @Serial
    private static final long serialVersionUID = 42L;
    @Null(message = "field.error.null")
    private final Long id;
    @NotNull(message = "field.error.not.null")
    @Min(value = 1, message = "field.error.min._1")
    private final Long timeTableId;
    @Null(message = "field.error.null")
    private final Integer orderPrice;
    @NotEmpty(message = "field.error.empty.collection")
    private final Set<Short> seats;
    @NotBlank(message = "field.error.empty.collection")
    private final String customer;

    private OrderDto(Builder builder) {
        this.id = builder.id;
        this.timeTableId = builder.timeTableId;
        this.orderPrice = builder.orderPrice;
        this.seats = builder.seats;
        this.customer = builder.customer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<Long> getId() {
        return ofNullable(id);
    }

    public Long getTimeTableId() {
        return timeTableId;
    }

    public Optional<Integer> getOrderPrice() {
        return ofNullable(orderPrice);
    }

    public Set<Short> getSeats() {
        return seats;
    }

    public String getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDto orderDto)) return false;
        return Objects.equals(getId(), orderDto.getId()) && Objects.equals(getTimeTableId(), orderDto.getTimeTableId()) && Objects.equals(getOrderPrice(), orderDto.getOrderPrice()) && Objects.equals(getSeats(), orderDto.getSeats()) && Objects.equals(getCustomer(), orderDto.getCustomer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimeTableId(), getOrderPrice(), getSeats(), getCustomer());
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private Long id;
        private Long timeTableId;
        private Integer orderPrice;
        private Set<Short> seats;
        private String customer;

        public OrderDto build() {
            return new OrderDto(this);
        }

        public OrderDto.Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public OrderDto.Builder timeTableId(final Long timeTableId) {
            this.timeTableId = timeTableId;
            return this;
        }

        public OrderDto.Builder orderPrice(final Integer orderPrice) {
            this.orderPrice = orderPrice;
            return this;
        }

        public OrderDto.Builder seats(final Set<Short> seats) {
            this.seats = seats;
            return this;
        }

        public OrderDto.Builder customer(final String customer) {
            this.customer = customer;
            return this;
        }

    }
}
