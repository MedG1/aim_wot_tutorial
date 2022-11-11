package tn.supcom.entities;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


@MappedSuperclass
public class PersonWithDetails<ID extends Serializable> extends Person<ID>{

    private String email;
    @Column(name = "valid_email")
    private Boolean validEmail;
    private String phone;
    @Column(name = "valid_phone")
    private Boolean validPhone;
    private LocalDate birthday;
    @Column(nullable = false)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", columnDefinition = "BIGINT", referencedColumnName = "address_id", nullable = true)
    private Address address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getValidEmail() {
        return validEmail;
    }

    public void setValidEmail(Boolean validEmail) {
        this.validEmail = validEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getValidPhone() {
        return validPhone;
    }

    public void setValidPhone(Boolean validPhone) {
        this.validPhone = validPhone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonWithDetails<?> that = (PersonWithDetails<?>) o;
        return Objects.equals(email, that.email) && Objects.equals(validEmail, that.validEmail) && Objects.equals(phone, that.phone) && Objects.equals(validPhone, that.validPhone) && Objects.equals(birthday, that.birthday) && gender == that.gender && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, validEmail, phone, validPhone, birthday, gender, address);
    }

    @Override
    public String toString() {
        return "{" +
                "\"super\":" + super.toString() +
                ", \"email\":\"" + email + '\"' +
                ", \"validEmail\":" + validEmail +
                ", \"phone\":\"" + phone + '\"' +
                ", \"validPhone\":" + validPhone +
                ", \"birthday\":\"" + birthday + '\"' +
                ", \"gender\":\"" + gender + '\"' +
                ", \"address\":" + address +
                '}';
    }
}
