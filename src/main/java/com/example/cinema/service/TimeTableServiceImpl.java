package com.example.cinema.service;

import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.dao.TimeTableRepository;
import com.example.cinema.dao.TimeTableSpecification;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.TimeTableDto;
import com.example.cinema.service.converters.TimeTableConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.example.cinema.core.ValidatorHelper.validateLong;

@Service
public class TimeTableServiceImpl implements TimeTableService {
    private final TimeTableRepository repository;
    private final TimeTableSpecification specification;
    private final TimeTableConverter converter;

    public TimeTableServiceImpl(TimeTableRepository repository, TimeTableSpecification specification, TimeTableConverter converter) {
        this.repository = repository;
        this.specification = specification;
        this.converter = converter;
    }

    @Override
    @Transactional(readOnly = true)
    public TimeTableDto getByIdEagerAsDto(Long id) {
        validateLong(id);
        final TimeTable timeTable = repository.getTimeTableByIdEager(id).orElseThrow(() -> new EntityNotFoundException("TimeTable not found by id = " + id));
        return converter.toDto(timeTable);
    }

    @Override
    @Transactional(readOnly = true)
    public TimeTable getByIdEager(Long id) {
        validateLong(id);
        return repository.getTimeTableByIdEager(id).orElseThrow(() -> new EntityNotFoundException("TimeTable not found by id = " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TimeTable getByIdWithCinemaHallOnlyEager(Long id) {
        validateLong(id);
        return repository.getTimeTableByIdAndCinemaHallOnlyEager(id).orElseThrow(() -> new EntityNotFoundException("TimeTable not found by id = " + id));

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
    public boolean ifTimeTableExistsByCinemaHallIdLegacy(Long id) {
        validateLong(id);
        return repository.ifTimeTableExistsByCinemaHallIdLegacy(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean ifTimeTableExistsByCinemaHallIdFuture(Long id) {
        validateLong(id);
        return repository.ifTimeTableExistsByCinemaHallIdFuture(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeTable> getByIdOptionalLazy(Long id) {
        validateLong(id);
        return repository.findById(id);
    }

}
