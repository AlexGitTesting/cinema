package com.example.cinema.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents understandable  order DTO for customer.
 *
 * @author Alexandr Yefremov
 */
public record OrderHumanDto(String movieTitle,
                            String cinemaHallName,
                            Integer orderPrice,
                            LocalDateTime startSession,
                            Set<Short> seats,
                            String customer,
                            Long timeTableId,
                            Long id) {


}
