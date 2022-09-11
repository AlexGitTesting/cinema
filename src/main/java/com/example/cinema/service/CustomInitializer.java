package com.example.cinema.service;

import com.example.cinema.core.SeatType;
import com.example.cinema.dao.CinemaHallRepository;
import com.example.cinema.dao.MovieRepository;
import com.example.cinema.dao.TimeTableRepository;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.Movie;
import com.example.cinema.domain.TimeTable;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;

//@Component
public class CustomInitializer implements ApplicationRunner {

    private final CinemaHallRepository repository;
    private final MovieRepository movieRepository;
    private final TimeTableRepository timeTableRepository;

    public CustomInitializer(CinemaHallRepository repository, MovieRepository movieRepository, TimeTableRepository timeTableRepository) {
        this.repository = repository;
        this.movieRepository = movieRepository;
        this.timeTableRepository = timeTableRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        crete();
    }

    @Transactional
    public void crete() {
        final CinemaHall red = new CinemaHall(null, "Go", (short) 10);
        EnumMap<SeatType, HashSet<Integer>> seatsType = new EnumMap<>(SeatType.class);

        HashSet<Integer> value = new HashSet<>(List.of(1, 2, 3));
        HashSet<Integer> set2 = new HashSet<>(List.of(10, 11, 12, 13));
        HashSet<Integer> set3 = new HashSet<>(List.of(14, 15, 16));
        HashSet<Integer> put = seatsType.put(SeatType.BLIND, value);
        final HashSet<Integer> put1 = seatsType.put(SeatType.LUXURY, set2);
        final HashSet<Integer> put2 = seatsType.put(SeatType.KISSES, set3);
        red.setSeatsType(seatsType);
        final CinemaHall savedHall = repository.save(red);
        final Movie movie = new Movie(null, "Title", (short) 50, "producer");
        final Movie movieSaved = movieRepository.save(movie);
        final TimeTable timeTable = new TimeTable(null, movieSaved, savedHall, LocalDateTime.now(), (short) 75, false);
        timeTable.getClosedSeats().add((short) 1);
        timeTable.getClosedSeats().add((short) 3);
        timeTable.getClosedSeats().add((short) 5);
        timeTableRepository.save(timeTable);
    }
}
