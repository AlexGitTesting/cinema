package com.example.cinema.service;

import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.dto.TimeTableDto;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

public interface TimeTableService {
    Page<TimeTableDto> getByFiler(@NonNull TimeTableQueryFilter filter);
}
