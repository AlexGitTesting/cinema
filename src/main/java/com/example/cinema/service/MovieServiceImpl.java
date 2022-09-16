package com.example.cinema.service;

import com.example.cinema.core.RequiredFieldsForCreation;
import com.example.cinema.core.RequiredFieldsForUpdating;
import com.example.cinema.dao.MovieQueryFilter;
import com.example.cinema.dao.MovieRepository;
import com.example.cinema.dao.MovieSpecification;
import com.example.cinema.dao.TimeTableRepository;
import com.example.cinema.domain.Movie;
import com.example.cinema.dto.MovieDto;
import com.example.cinema.service.converters.MovieConverter;
import com.example.cinema.service.validator.ValidationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link MovieService}.
 *
 * @author Alexandr Yefremov
 */
@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository repository;
    private final MovieSpecification specification;
    private final MovieConverter converter;
    private final TimeTableRepository tableRepository;
    private final ValidationService validator;

    public MovieServiceImpl(MovieRepository repository, MovieSpecification specification, MovieConverter converter, TimeTableRepository tableRepository, ValidationService validator) {
        this.repository = repository;
        this.specification = specification;
        this.converter = converter;
        this.tableRepository = tableRepository;
        this.validator = validator;
    }


    @Override
    @Transactional
    public MovieDto create(MovieDto dto) {
        validator.validate(dto, Movie.class.getSimpleName(), RequiredFieldsForCreation.class);
        final Movie saved = repository.save(converter.toDomain(dto));
        return converter.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDto getById(Long id) {
        return converter.toDto(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("resource.by.id.not.found")));
    }

    @Override
    @Transactional
    public MovieDto update(MovieDto dto) {
        validator.validate(dto, MovieDto.class.getSimpleName(), RequiredFieldsForUpdating.class);
        final Movie movie = repository.findById(dto.getId().orElseThrow())
                .orElseThrow(() -> new EntityNotFoundException("resource.by.id.not.found"));
        converter.toDomain(dto, movie);
        return converter.toDto(movie);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (tableRepository.ifTimeTableExistsByMovieIdInFuture(id)) {
            throw new IllegalArgumentException("movie.can.not.remove");
        }
        repository.deleteById(id);
        return true;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<MovieDto> getByFiler(@NonNull MovieQueryFilter filter) {
        requireNonNull(filter, "MovieQueryFilter is null");
        final Page<Movie> page = repository.findAll(specification.getByFilter(filter), PageRequest.of(filter.getPage(), filter.getLimit()));
        return page.map(converter::toDto);
    }
}
