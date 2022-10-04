package com.example.cinema.web;

import com.example.cinema.dto.OrderDto;
import com.example.cinema.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.example.cinema.core.ValidatorHelper.validateParam;

@RestController
@RequestMapping(path = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Order controller", description = "Handles HTTP requests connected with order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Creates new order")
    @PostMapping(path = "/create.json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    OrderDto createOrder(@RequestBody OrderDto dto) {
        return orderService.createOrder(dto);
    }

    @Operation(summary = "Gets order.", description = "Gets order by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/get-order.json")
    OrderDto getById(@Parameter(description = "Order id. Not null and greater then 0") @PathVariable("id") Long id) {
        validateParam(id);
        return orderService.getById(id);
    }

    @Operation(summary = "Removes order by id")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/delete.json")
    boolean delete(@Parameter(description = "Order id. Not null and greater then 0") @PathVariable("id") Long id) {
        validateParam(id);
        return orderService.deleteOrder(id);
    }

}
