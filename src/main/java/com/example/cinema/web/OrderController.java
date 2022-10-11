package com.example.cinema.web;

import com.example.cinema.dto.OrderDto;
import com.example.cinema.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.example.cinema.core.ValidatorHelper.validateParam;

@RestController
@RequestMapping(path = "${url.base.order}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Order controller", description = "Handles HTTP requests connected with order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Creates new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order is created", content = @Content(schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Not valid dto"
                    , content = @Content(schema = @Schema(implementation = String.class)
                    , examples = {
                    @ExampleObject(name = "Validation exception message", value = """
                            Validation exception:
                            Field: orderPrice,  message: The field must be null,
                            Field: id,  message: The field must be null,
                            Field: seats,  message: Collection must be not empty,
                            Field: timeTableId,  message: The field must have a value,
                            Field: customer,  message: Collection must be not empty,"""),
                    @ExampleObject(name = "All seats are sold", value = "All seats are sold"),
                    @ExampleObject(name = "Seats are not related to current cinema hall", value = "Booked seats are out of the range of the current cinema hall"),
                    @ExampleObject(name = "Seat has already booked", value = "One of these seats are already closed, please try others"),
                    @ExampleObject(name = "Total price became bigger then Integer.MAX", value = "Total price of the order became more then maximum allowable value"),
                    @ExampleObject(name = "Session has already started", value = "You can not create order for movie that has already started")})),
            @ApiResponse(responseCode = "404", description = "Timetable by id not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Timetable with id \"?\" not found")))})
    @PostMapping(path = "${url.create}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    OrderDto createOrder(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order dto for creation",
            content = @Content(examples = {
                    @ExampleObject(name = "Valid dto", value = """
                            {"id":null,"timeTableId":17,"orderPrice":null,"seats":[3,4,5],"customer":"customer"}"""),
                    @ExampleObject(name = "Timetable not exists", value = """
                            {"id":null,"timeTableId":1500,"orderPrice":null,"seats":[1,5,4],"customer":"customer"}"""),
                    @ExampleObject(name = "All seats are sold ", value = """
                            {"id":null,"timeTableId":27,"orderPrice":null,"seats":[1,5,4],"customer":"customer"}"""),
                    @ExampleObject(name = "Seats have already sold ", value = """
                            {"id":null,"timeTableId":26,"orderPrice":null,"seats":[1,2,5],"customer":"customer"}"""),
                    @ExampleObject(name = "Seats are not related to the cinema hall ", value = """
                            {"id":null,"timeTableId":26,"orderPrice":null,"seats":[24,57],"customer":"customer"}"""),
                    @ExampleObject(name = "Session has already started ", value = """
                            {"id":null,"timeTableId":31,"orderPrice":null,"seats":[1,4],"customer":"customer"}"""),
                    @ExampleObject(name = "Not valid dto", value = """
                            {"id":1,"timeTableId":null,"orderPrice":45,"seats":[],"customer":"  "}""")
            })
    ) @RequestBody OrderDto dto) {
        return orderService.createOrder(dto);
    }

    @Operation(summary = "Gets order by id.", description = "Gets order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order is found", content = @Content(schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameter",
                    headers = @Header(name = "Error message", schema = @Schema(implementation = String.class),
                            description = "Argument is invalid. Number must be not null and greater then 0")),
            @ApiResponse(responseCode = "404", description = "Order by id not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Order by id not found")))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "${url.get-by-id}")
    OrderDto getById(@Parameter(description = "Order id. Not null and greater then 0", examples = {
            @ExampleObject(name = "Valid id", value = "1"),
            @ExampleObject(name = "Invalid id,less then 0", value = "-5"),
            @ExampleObject(name = "Invalid id, null", value = "null"),
            @ExampleObject(name = "Not found", value = "1500")
    }) @PathVariable("id") Long id) {
        validateParam(id);
        return orderService.getById(id);
    }

    @Operation(summary = "Removes order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "true"))),
            @ApiResponse(responseCode = "400", headers = @Header(name = "Error message",
                    schema = @Schema(implementation = String.class), description = "Argument is invalid. Number must be not null and greater then 0")),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "Order by id ? not found")))
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "${url.delete}")
    boolean delete(@Parameter(description = "Order id. Not null and greater then 0", examples = {
            @ExampleObject(name = "Valid id", value = "1"),
            @ExampleObject(name = "Invalid id,less then 0", value = "-5"),
            @ExampleObject(name = "Invalid id, null", value = "null"),
            @ExampleObject(name = "Not found", value = "1500")
    }) @PathVariable("id") Long id) {
        validateParam(id);
        return orderService.deleteOrder(id);
    }

}
