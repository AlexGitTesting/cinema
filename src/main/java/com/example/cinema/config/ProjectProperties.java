package com.example.cinema.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Properties that exposes urls.
 *
 * @author Alexandr Yefremov
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "url")
@Validated
public class ProjectProperties {
    @NotBlank(message = "Property must be not blank")
    private final String create;
    @NotBlank(message = "Property must be not blank")
    private final String getById;
    @NotBlank(message = "Property must be not blank")
    private final String getByFilter;
    @NotBlank(message = "Property must be not blank")
    private final String update;
    @NotBlank(message = "Property must be not blank")
    private final String delete;
    @Valid
    private final Base base;

    public ProjectProperties(String create, String getById, String getByFilter, String update, String delete, Base base) {
        this.create = create;
        this.getById = getById;
        this.getByFilter = getByFilter;
        this.update = update;
        this.delete = delete;
        this.base = base;
    }

    public String getCreate() {
        return create;
    }

    public String getGetById() {
        return getById;
    }

    public String getGetByFilter() {
        return getByFilter;
    }

    public String getUpdate() {
        return update;
    }

    public String getDelete() {
        return delete;
    }

    public Base getBase() {
        return base;
    }

    public record Base(@NotBlank(message = "Property must be not blank") String order,
                       @NotBlank(message = "Property must be not blank") String movie,
                       @NotBlank(message = "Property must be not blank") String cinemaHall,
                       @NotBlank(message = "Property must be not blank") String timeTable) {

        public String getOrder() {
            return order;
        }

        public String getMovie() {
            return movie;
        }

        public String getCinemaHall() {
            return cinemaHall;
        }

        public String getTimeTable() {
            return timeTable;
        }
    }
}
