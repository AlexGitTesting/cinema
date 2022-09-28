package com.example.cinema.service;

import com.example.cinema.dao.OrderRepository;
import com.example.cinema.domain.OrderTable;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.OrderDto;
import com.example.cinema.service.converters.OrderConverter;
import com.example.cinema.service.validator.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

/**
 * Implementation of {@link OrderService}.
 *
 * @author Alexandr Yefremov
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final OrderConverter converter;
    private final TimeTableService tableService;
    private final ValidationService validator;

    public OrderServiceImpl(OrderRepository repository, OrderConverter converter, TimeTableService tableService, ValidationService validator) {
        this.repository = repository;
        this.converter = converter;
        this.tableService = tableService;
        this.validator = validator;
    }

    @Override
    public OrderDto createOrder(OrderDto dto) {
        validator.validate(dto, OrderDto.class.getSimpleName());
        final TimeTable timeTable = tableService.getByIdEager(dto.getTimeTableId());
        if (timeTable.getStartSession().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("start.session.already.been");
        }
        OrderTable orderTable = converter.toDomain(dto);
        orderTable.setOrderPrice(evaluateTotalPrice(timeTable, dto));
        timeTable.addClosedSeats(dto.getSeats());
        orderTable.setTimeTable(timeTable);
        saveOrder(orderTable);
        tableService.updateTimeTable(timeTable);
        return converter.toDto(orderTable);

    }

    @Transactional
    @Override
    public OrderTable saveOrder(OrderTable order) {
        return repository.save(order);
    }

    /**
     * Evaluates total price of the order.
     *
     * @param timeTable that refers to the current order
     * @param dto       Order dto
     * @return order's sum
     * @throws IllegalStateException if order's sum became more then Integer.MAX_VALUE
     */
    private int evaluateTotalPrice(TimeTable timeTable, OrderDto dto) throws IllegalStateException {
        long totalPrice = 0L;
        for (Short seat :
                dto.getSeats()) {
            totalPrice += Math.round(timeTable.getBasePrice() * timeTable.getCinemaHall().getSeatTypeBySeatNumber(seat).getCoefficient());
        }
        if (totalPrice > Integer.MAX_VALUE) {
            throw new IllegalStateException("Total price of the order became more then maximum allowable value");
        }
        return (int) totalPrice;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long id) {
        final OrderTable order = repository.findOrderTableById(id).orElseThrow(() -> new EntityNotFoundException("Order by id not found"));
        if (order.getTimeTable().getStartSession().isBefore(LocalDateTime.now())) {
            return false;
        }
        if (!order.getSeats().isEmpty()) {
            order.getTimeTable().reopenClosedSeats(order.getSeats());
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        return converter.toDto(repository.findOrderTableById(id).orElseThrow(() -> new EntityNotFoundException("Order by id not found")));
    }

}
