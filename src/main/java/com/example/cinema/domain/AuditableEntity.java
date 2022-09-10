package com.example.cinema.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract class to hold audit data.
 *
 * @author Alexandr Yefremov
 * @see BaseEntity
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public abstract class AuditableEntity extends BaseEntity {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public AuditableEntity() {
    }

    public AuditableEntity(Long id) {
        super(id);
    }

    public AuditableEntity(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        super(id);
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditableEntity that)) return false;
        if (!super.equals(o)) return false;

        return getCreatedDate().equals(that.getCreatedDate()) && getModifiedDate().equals(that.getModifiedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCreatedDate(), getModifiedDate());
    }

    @Override
    public String toString() {
        return "AuditableEntity{" +
                "createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
