package com.example.cinema.dto;

import com.example.cinema.core.DtoMarker;
import com.example.cinema.core.SeatType;
import com.example.cinema.domain.CinemaHall;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashSet;

/**
 * Represents dto of {@link CinemaHall}.
 *
 * @author Alexandr Yefremov
 */
public record CinemaHallDto(
        @NotNull(message = "field.error.not.null") @Min(value = 1, message = "field.error.min._1") Long id,
        @NotBlank(message = "field.error.blank") @Size(max = 20, min = 1, message = "field.error.size.incorrect") String name,
        @NotNull(message = "field.error.not.null") @Min(value = 1, message = "field.error.min._1") Short seatsAmount,
        @NotEmpty(message = "field.error.empty.collection") EnumMap<SeatType, HashSet<Short>> seatsType) implements Serializable, DtoMarker {
    @Serial
    private static final long serialVersionUID = 42L;

}
