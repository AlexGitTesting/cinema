package com.example.cinema.service;

import com.example.cinema.dao.CinemaHallRepository;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.dto.CinemaHallDto;
import com.example.cinema.service.converters.CinemaHallConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.example.cinema.core.ValidatorHelper.validateLong;

/**
 * Implementation of the CinemaHallService.
 *
 * @author Alexandr Yefremov
 */
@Service
public class CinemaHallServiceImpl implements CinemaHallService {

    private final CinemaHallConverter converter;
    private final CinemaHallRepository repository;
    private final TimeTableService tableService;

    public CinemaHallServiceImpl(CinemaHallConverter converter, CinemaHallRepository repository, TimeTableService tableService) {
        this.converter = converter;
        this.repository = repository;
        this.tableService = tableService;
    }

    @Override
    @Transactional
    public CinemaHallDto createCinemaHall(CinemaHallDto dto) {// TODO: 15.09.2022 validate
        if (dto.id() != null) throw new IllegalArgumentException("Cinema hall must  be new");
        final CinemaHall cinemaHall = converter.toDomain(dto);
        return converter.toDto(repository.save(cinemaHall));
    }

    @Override
    @Transactional(readOnly = true)
    public CinemaHallDto readById(Long id) {
        final CinemaHall hall = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cinema not fount"));
        return converter.toDto(hall);
    }

    @Override
    @Transactional
    public CinemaHallDto update(CinemaHallDto dto) {// TODO: 15.09.2022 validate
        if (dto.id() == null) throw new IllegalArgumentException("Cinema hall must not be new");
        if (tableService.ifTimeTableExistsByCinemaHallIdFuture(dto.id())) {
            throw new IllegalArgumentException("You can not update cinema hall, because there will sessions in this cinema hall in the future");
        }
        final CinemaHall cinemaHall = repository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Cinema not fount"));
        converter.toDomainTarget(dto, cinemaHall);
        return converter.toDto(repository.save(cinemaHall));
    }

    @Override
    @Transactional
    public Long delete(Long id) {
        validateLong(id);
        if (!tableService.ifTimeTableExistsByCinemaHallIdFuture(id)) {
            repository.deleteById(id);
            return id;
        }
        throw new IllegalArgumentException("You can not delete cinema hall, because there are timetables references on this cinema hall");

    }
}
