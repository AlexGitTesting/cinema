package com.example.cinema.service;

import com.example.cinema.core.CustomConstraintException;
import com.example.cinema.core.RequiredFieldsForCreation;
import com.example.cinema.core.RequiredFieldsForUpdating;
import com.example.cinema.dao.CinemaHallRepository;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.dto.CinemaHallDto;
import com.example.cinema.service.converters.CinemaHallConverter;
import com.example.cinema.service.validator.ValidationService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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
    public CinemaHallDto createCinemaHall(CinemaHallDto dto) throws CustomConstraintException {
        validator.validate(dto, dto.getClass().getSimpleName(), RequiredFieldsForCreation.class);
        final CinemaHall cinemaHall = converter.toDomain(dto);
        return converter.toDto(saveCinemaHallAndCheckConstraint(dto, cinemaHall));
    }

    @Override
    @Transactional(readOnly = true)
    public CinemaHallDto readById(Long id) {
        final CinemaHall hall = repository.getCinemaHall(id).orElseThrow(() -> new EntityNotFoundException("not.found.cinema.hall"));
        return converter.toDto(hall);
    }

    @Override
    @Transactional
    public CinemaHallDto update(CinemaHallDto dto) throws CustomConstraintException {
        validator.validate(dto, CinemaHallDto.class.getSimpleName(), RequiredFieldsForUpdating.class);
        if (tableService.ifTimeTableExistsByCinemaHallIdInFuture(dto.id())) {
            throw new IllegalArgumentException(CAN_NOT_UPDATE);
        }
        final CinemaHall cinemaHall = repository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("not.found.cinema.hall"));
        converter.toDomainTarget(dto, cinemaHall);
        return converter.toDto(saveCinemaHallAndCheckConstraint(dto, cinemaHall));
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

    /**
     * Saves cinema hall and catches ConstraintViolationException if name not unique.
     *
     * @param dto        {@link CinemaHallDto}
     * @param cinemaHall {@link CinemaHall} will be saved
     * @return saved cinema hall
     * @throws CustomConstraintException if {@link CinemaHallDto#name()} contains not unique name
     */
    private CinemaHall saveCinemaHallAndCheckConstraint(CinemaHallDto dto, CinemaHall cinemaHall) throws CustomConstraintException {
        CinemaHall save = cinemaHall;
        try {
            save = repository.saveAndFlush(cinemaHall);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause().getClass().equals(ConstraintViolationException.class)) {
                if (((ConstraintViolationException) e.getCause()).getConstraintName().equalsIgnoreCase("cinema_hall_name_not_unique")) {
                    throw new CustomConstraintException(e.getCause(), "cinema.hall.name.not.unique", new Object[]{dto.name()});
                }
            }
        }
        return save;
    }
}
