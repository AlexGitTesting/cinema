package com.example.cinema.web;

import com.example.cinema.core.CreateUpdateContract;
import com.example.cinema.core.DeleteContract;
import com.example.cinema.core.GetByIdContract;
import com.example.cinema.dto.CinemaHallDto;
import com.example.cinema.service.CinemaHallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.cinema.core.ValidatorHelper.validateParam;

@RestController
@RequestMapping(path = "${url.base.cinema_hall}")
@Tag(name = "Cinema hall controller.", description = "Handles HTTP requests connected with Cinema hall")
public class CinemaHallController implements CreateUpdateContract<CinemaHallDto>, DeleteContract, GetByIdContract<CinemaHallDto> {
    private final CinemaHallService service;
    private final ResponseExceptionHandler exceptionHandler;

    public CinemaHallController(CinemaHallService service, ResponseExceptionHandler exceptionHandler) {
        this.service = service;
        this.exceptionHandler = exceptionHandler;
    }

    @Operation(summary = "Creates new cinema hall.")
    @Override
    public CinemaHallDto create(CinemaHallDto dto) {
        return service.createCinemaHall(dto);
    }

    @Operation(summary = "Updates existed cinema hall.")
    @Override
    public CinemaHallDto update(CinemaHallDto dto) {
        return service.update(dto);
    }

    @Override
    public ResponseEntity<Object> delete(Long id) {
        validateParam(id);
        final Long delete = service.delete(id);
        assert id.equals(delete);
        final String message = exceptionHandler.prepareLocalizedMessage("cinema.hall.removed"
                , String.format("Cinema hall with id %d successfully removed.", id)
                , new Object[]{id});
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "Retrieves cinema hall.", description = "Retrieves cinema hall by id")
    @Override
    public CinemaHallDto getById(Long id) {
        validateParam(id);
        return service.readById(id);
    }
}
