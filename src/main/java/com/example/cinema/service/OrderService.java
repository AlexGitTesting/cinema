package com.example.cinema.service;

import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.OrderDto;
import com.example.cinema.dto.OrderHumanDto;

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
     * @return created OrderHumanDto
     * @throws IllegalArgumentException if you try save order for movie that has already started
     * @throws EntityNotFoundException  if {@link TimeTable} related to this Order not found
     * @throws IllegalStateException    if all seats are sold
     * @see TimeTable#addClosedSeats(Set) will be used to add booked seats
     */
    OrderHumanDto createOrder(OrderDto dto) throws IllegalArgumentException, EntityNotFoundException, IllegalStateException;

    /**
     * Removes Order.
     *
     * @param id order's id.
     * @return true if ok.
     * @throws EntityNotFoundException if order not found by id.
     * @throws IllegalStateException   if you try remove order which session has already started or finished
     */
    boolean deleteOrder(Long id) throws EntityNotFoundException, IllegalStateException;


    /**
     * Returns OrderHumanDto by id.
     *
     * @return {@link OrderHumanDto}
     * @throws EntityNotFoundException id dto not found
     */
    OrderHumanDto getById(Long id) throws EntityNotFoundException;

}
