package com.example.cinema.service;

import com.example.cinema.dao.*;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.Movie;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.BasisTimeTable;
import com.example.cinema.dto.TimeTableDto;
import com.example.cinema.service.converters.TimeTableConverter;
import com.example.cinema.service.validator.ValidationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.cinema.core.ValidatorHelper.validateLong;

/**
 * Implementation of {@link TimeTableService}
 *
 * @author Alexandr Yefremov
 */
@Service
public class TimeTableServiceImpl implements TimeTableService {
    private final TimeTableRepository repository;
    private final TimeTableSpecification specification;
    private final TimeTableConverter converter;
    private final MovieRepository movieRepository;
    private final CinemaHallRepository cinemaHallRepository;
    private final ValidationService validator;


    public TimeTableServiceImpl(TimeTableRepository repository, TimeTableSpecification specification,
                                TimeTableConverter converter, MovieRepository movieRepository,
                                CinemaHallRepository cinemaHallRepository, ValidationService validator) {
        this.repository = repository;
        this.specification = specification;
        this.converter = converter;
        this.movieRepository = movieRepository;
        this.cinemaHallRepository = cinemaHallRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public TimeTableDto createNew(BasisTimeTable dto) {
        validator.validate(dto, BasisTimeTable.class.getSimpleName());
        if (dto.startSession().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("start.session.not.correct");
        }
        final Movie movie = movieRepository.findById(dto.movieId()).orElseThrow(() -> new EntityNotFoundException("not.found.movie"));
        final CinemaHall cinemaHall = cinemaHallRepository.getCinemaHall(dto.cinemaHallId()).orElseThrow(() -> new EntityNotFoundException("not.found.cinema.hall"));
        final TimeTable table = new TimeTable(null, movie, cinemaHall, dto.startSession(), dto.basePrice(), false);
        return converter.toDto(repository.save(table));
    }

    @Override
    @Transactional(readOnly = true)
    public TimeTableDto getByIdEagerAsDto(Long id) {
        final TimeTable timeTable = repository.getTimeTableByIdEagerReadOnly(id).orElseThrow(() -> new EntityNotFoundException("TimeTable not found by id = " + id));
        return converter.toDto(timeTable);
    }

    @Override
    @Transactional(readOnly = true)
    public TimeTable getByIdEager(Long id) {
        validateLong(id);
        return repository.getTimeTableByIdEagerReadOnly(id).orElseThrow(() -> new EntityNotFoundException("TimeTable not found by id = " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TimeTableDto> getByFiler(@NonNull TimeTableQueryFilter filter) {
        final Page<TimeTable> tables = repository.findAll(specification.getByFilter(filter), PageRequest.of(filter.getPage(), filter.getLimit()));
        return tables.map(converter::toDto);
    }

    @Override
    @Transactional
    public TimeTable updateTimeTable(TimeTable table) {
        validateLong(table.getId());
        validateLong(table.getMovie().getId());
        validateLong(table.getCinemaHall().getId());
        return repository.save(table);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean ifTimeTableExistsByCinemaHallIdInFuture(Long id) {
        validateLong(id);
        return repository.ifTimeTableExistsByCinemaHallIdInFuture(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeTable> getByIdOptionalLazy(Long id) {
        validateLong(id);
        return repository.findById(id);
    }

}
