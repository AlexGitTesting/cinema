package com.example.cinema.service;

import com.example.cinema.core.RequiredFieldsForCreation;
import com.example.cinema.core.RequiredFieldsForUpdating;
import com.example.cinema.dao.CinemaHallRepository;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.dto.CinemaHallDto;
import com.example.cinema.service.converters.CinemaHallConverter;
import com.example.cinema.service.validator.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Implementation of the CinemaHallService.
 *
 * @author Alexandr Yefremov
 */
@Service
public class CinemaHallServiceImpl implements CinemaHallService {

    public static final String CAN_NOT_UPDATE = "You can not update cinema hall, because there will sessions in this cinema hall in the future";
    public static final String CAN_NOT_DELETE = "You can not delete cinema hall, because there are timetables references on this cinema hall";
    private final CinemaHallConverter converter;
    private final CinemaHallRepository repository;
    private final TimeTableService tableService;
    private final ValidationService validator;

    public CinemaHallServiceImpl(CinemaHallConverter converter, CinemaHallRepository repository, TimeTableService tableService, ValidationService validator) {
        this.converter = converter;
        this.repository = repository;
        this.tableService = tableService;
        this.validator = validator;
    }

    @Override
    @Transactional
    public CinemaHallDto createCinemaHall(CinemaHallDto dto) {
        validator.validate(dto, dto.getClass().getSimpleName(), RequiredFieldsForCreation.class);
        final CinemaHall cinemaHall = converter.toDomain(dto);
        return converter.toDto(repository.save(cinemaHall));
    }

    @Override
    @Transactional(readOnly = true)
    public CinemaHallDto readById(Long id) {
        final CinemaHall hall = repository.getCinemaHall(id).orElseThrow(() -> new EntityNotFoundException("not.found.cinema.hall"));
        return converter.toDto(hall);
    }

    @Override
    @Transactional
    public CinemaHallDto update(CinemaHallDto dto) {
        validator.validate(dto, CinemaHallDto.class.getSimpleName(), RequiredFieldsForUpdating.class);
        if (tableService.ifTimeTableExistsByCinemaHallIdInFuture(dto.id())) {
            throw new IllegalArgumentException(CAN_NOT_UPDATE);
        }
        final CinemaHall cinemaHall = repository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("not.found.cinema.hall"));
        converter.toDomainTarget(dto, cinemaHall);
        return converter.toDto(repository.save(cinemaHall));
    }

    @Override
    @Transactional
    public Long delete(Long id) {
        if (!tableService.ifTimeTableExistsByCinemaHallIdInFuture(id)) {
            repository.deleteById(id);
            return id;
        }
        throw new IllegalArgumentException(CAN_NOT_DELETE);
    }
}
