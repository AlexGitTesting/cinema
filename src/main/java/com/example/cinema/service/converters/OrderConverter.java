package com.example.cinema.service.converters;

import com.example.cinema.domain.OrderTable;
import com.example.cinema.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderConverter {
    @Mapping(target = "timeTableId", expression = "java(source.getId())")
    @Mapping(target = "customer", source = "customer")
    OrderDto toDto(OrderTable source);

    @Mapping(target = "id", expression = "java(source.getId().orElse(null))")
    @Mapping(target = "timeTable", expression = "java(new com.example.cinema.domain.TimeTable(source.getTimeTableId()))")
    OrderTable toDomain(OrderDto source);

// FIXME: 12.09.2022
//    default TimeTable toTimeTable(Long source){
//        final TimeTable timeTable = new TimeTable();
//        timeTable.setId(source);
//        return timeTable;
//    }
}
