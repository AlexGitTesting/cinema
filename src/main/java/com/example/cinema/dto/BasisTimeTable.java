package com.example.cinema.dto;

import com.example.cinema.domain.TimeTable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents DTO of {@link TimeTable} to create new table
 *
 * @author Alexandr Yefremov
 */
public record BasisTimeTable(
        @NotNull(message = "field.error.not.null") @Min(value = 1, message = "field.error.min._1") Long movieId,
        @NotNull(message = "field.error.not.null") @Min(value = 1, message = "field.error.min._1") Long cinemaHallId,
        @NotNull(message = "field.error.not.null") LocalDateTime startSession,
        @NotNull(message = "field.error.not.null") @Min(value = 0, message = "field.error.min._0") Short basePrice) implements Serializable {
    @Serial
    private static final long serialVersionUID = 42L;
}
