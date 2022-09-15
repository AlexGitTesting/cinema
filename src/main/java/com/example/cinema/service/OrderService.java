package com.example.cinema.service;

import com.example.cinema.domain.OrderTable;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.OrderDto;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

/**
 * Contract for Order service.
 *
 * @author Alexandr Yefremov
 */
public interface OrderService {
    /**
     * Creates new order.
     *
     * @param dto {@link OrderDto}
     * @return created OrderDto
     * @throws IllegalArgumentException if dto is null or you try saveOrder order for movie that has already started
     * @throws EntityNotFoundException  if {@link TimeTable} related to this Order not found
     * @see TimeTable#addClosedSeats(Set) will be used to add booked seats
     */
    OrderDto createOrder(OrderDto dto) throws IllegalArgumentException, EntityNotFoundException;

    /**
     * Removes Order.
     *
     * @param id order's id.
     * @return true if ok.
     * @throws EntityNotFoundException if order not found by id.
     */
    boolean deleteOrder(Long id) throws EntityNotFoundException;


    /**
     * Returns OrderDto by id.
     *
     * @return {@link OrderDto}
     * @throws EntityNotFoundException id dto not found
     */
    OrderDto getById(Long id) throws EntityNotFoundException;

    /**
     * Saves order
     *
     * @param order order
     * @return saved order
     */
    OrderTable saveOrder(OrderTable order);

}
