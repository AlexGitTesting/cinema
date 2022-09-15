package com.example.cinema.service.converters;

import com.example.cinema.domain.OrderTable;
import com.example.cinema.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for Order converter.
 *
 * @author Alexandr Yefremov
 */
@Mapper(componentModel = "spring")
public interface OrderConverter {
    @Mapping(target = "timeTableId", expression = "java(source.getTimeTable().getId())")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "orderPrice", expression = "java(source.getOrderPrice().orElse(null))")
    OrderDto toDto(OrderTable source);

    @Mapping(target = "id", expression = "java(source.getId().orElse(null))")
    @Mapping(target = "orderPrice", ignore = true)
    @Mapping(target = "timeTable", expression = "java(new com.example.cinema.domain.TimeTable(source.getTimeTableId()))")
    OrderTable toDomain(OrderDto source);
}
