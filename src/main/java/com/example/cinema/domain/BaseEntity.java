package com.example.cinema.domain;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Class provides id and version to all related entities.
 *
 * @author Alexandr Yefremov
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 42L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private long version;

    public BaseEntity() {
    }

    public BaseEntity(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", version=" + version +
                '}';
    }
}
