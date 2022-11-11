package tn.supcom.entities;
import jakarta.persistence.*;
import tn.supcom.util.Identity;

import java.util.Objects;

@Entity
@Table(name = "users")
@EntityListeners({AuditListener.class})
public class User extends PersonWithDetails<Long> implements Auditable, Identity {
    @Column(length = 191, unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(name = "permission_level", columnDefinition = "BIGINT default 0")
    private Long permissionLevel;
    @Embedded
    private AuditFields auditFields;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(Long permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    @Override
    public AuditFields getAuditFields() {
        return auditFields;
    }

    @Override
    public void setAuditFields(AuditFields auditFields) {
        this.auditFields = auditFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password) && permissionLevel.equals(user.permissionLevel) && auditFields.equals(user.auditFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, permissionLevel, auditFields);
    }

    @Override
    public String getName() {
        return getForename() + " " + getSurname();
    }

    @Override
    public String toString() {
        return "{" +
                "\"super\":" + super.toString() +
                ", \"username\":\"" + username + '\"' +
                ", \"password\":\"" + password + '\"' +
                ", \"permissionLevel\":" + permissionLevel +
                ", \"auditFields\":" + auditFields +
                '}';
    }
}
