package com.example.cinema.service;

import com.example.cinema.dao.MovieQueryFilter;
import com.example.cinema.dao.MovieRepository;
import com.example.cinema.dao.MovieSpecification;
import com.example.cinema.dao.TimeTableRepository;
import com.example.cinema.domain.Movie;
import com.example.cinema.dto.MovieDto;
import com.example.cinema.service.converters.MovieConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public MovieServiceImpl(MovieRepository repository, MovieSpecification specification, MovieConverter converter, TimeTableRepository tableRepository) {
        this.repository = repository;
        this.specification = specification;
        this.converter = converter;
        this.tableRepository = tableRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public Page<MovieDto> getByFiler(MovieQueryFilter filter) {
        requireNonNull(filter, "MovieQueryFilter is null");
        final Page<Movie> page = repository.findAll(specification.getByFilter(filter), PageRequest.of(filter.getPage(), filter.getLimit()));
        return page.map(converter::toDto);
    }

    @Override
    @Transactional
    public MovieDto create(MovieDto dto) {// TODO: 15.09.2022 validate
        final Movie saved = repository.save(converter.toDomain(dto));
        return converter.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDto getById(Long id) {
        return converter.toDto(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie by id not fount")));
    }

    @Override
    @Transactional
    public MovieDto update(MovieDto dto) {
        final Movie movie = repository.findById(dto.getId().orElseThrow(() -> new IllegalArgumentException("Movie dto does not contain id")))
                .orElseThrow(() -> new EntityNotFoundException("Movie by id not fount"));
        converter.toDomain(dto, movie);
        return converter.toDto(movie);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (tableRepository.ifTimeTableExistsByMovieIdInFuture(id)) {
            throw new IllegalArgumentException("You can not delete movie, because there are timetables referring to it.");
        }
        repository.deleteById(id);
        return true;
    }

}
