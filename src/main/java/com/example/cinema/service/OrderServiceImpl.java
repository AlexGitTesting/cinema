package com.example.cinema.service;

import com.example.cinema.dao.OrderRepository;
import com.example.cinema.dao.TimeTableRepository;
import com.example.cinema.domain.OrderTable;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.OrderDto;
import com.example.cinema.service.converters.OrderConverter;
import com.example.cinema.service.validator.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static java.lang.String.format;

/**
 * Implementation of {@link OrderService}.
 *
 * @author Alexandr Yefremov
 */
@Service
public class OrderServiceImpl implements OrderService {
    public static final String TABLE_NOT_FOUND = "Timetable with id \"%d\" not found";
    public static final String TOTAL_PRICE_BECAME_HUGE = "Total price of the order became more then maximum allowable value";
    public static final String ORDER_NOT_FOUND = "Order by id %d not found";
    private final OrderRepository repository;
    private final OrderConverter converter;
    private final ValidationService validator;
    private final TimeTableRepository timeTableRepository;

    public OrderServiceImpl(OrderRepository repository, OrderConverter converter, ValidationService validator, TimeTableRepository timeTableRepository) {
        this.repository = repository;
        this.converter = converter;
        this.validator = validator;
        this.timeTableRepository = timeTableRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public OrderDto createOrder(OrderDto dto) {
        validator.validate(dto, OrderDto.class.getSimpleName());
        TimeTable timetable = timeTableRepository.getTimeTableByIdEagerModified(dto.getTimeTableId())
                .orElseThrow(() ->
                        new EntityNotFoundException(format(TABLE_NOT_FOUND, dto.getTimeTableId()))
                );
        if (timetable.getIsSold()) {
            throw new IllegalStateException("seats.all.sold");
        }
        if (timetable.getStartSession().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("start.session.already.been");
        }
        OrderTable orderTable = converter.toDomain(dto);
        timetable.addClosedSeats(dto.getSeats());
        timetable = timeTableRepository.save(timetable);
        orderTable.setTimeTable(timetable);
        orderTable.setOrderPrice(evaluateTotalPrice(timetable, dto));
        orderTable = repository.save(orderTable);
        return converter.toDto(orderTable);

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
            throw new IllegalStateException(TOTAL_PRICE_BECAME_HUGE);
        }
        return (int) totalPrice;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long id) {
        final OrderTable order = repository.findOrderByIdTimeTableEager(id)
                .orElseThrow(() -> new EntityNotFoundException(format(ORDER_NOT_FOUND, id)));
        if (order.getTimeTable().getStartSession().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("order.cannot.delete.session.started");
        }
        if (!order.getSeats().isEmpty()) {
            order.getTimeTable().reopenClosedSeats(order.getSeats());
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        return converter.toDto(repository.findOrderByIdTimeTableEager(id).orElseThrow(() -> new EntityNotFoundException(format(ORDER_NOT_FOUND, id))));
    }

}
