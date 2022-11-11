package tn.supcom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class AuditFields {
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
    @Column(name = "created_by", length = 191, nullable = false)
    private String createdBy;
    @Column(name = "updated_by", length = 191)
    private String updatedBy;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditFields that = (AuditFields) o;
        return createdAt.equals(that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && createdBy.equals(that.createdBy) && Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt, createdBy, updatedBy);
    }

    @Override
    public String toString() {
        return "{" +
                "\"createdAt\":\"" + createdAt + '\"' +
                ", \"updatedAt\":\"" + updatedAt + '\"' +
                ", \"createdBy\":\"" + createdBy + '\"' +
                ", \"updatedBy\":\"" + updatedBy + '\"' +
                '}';
    }
}
