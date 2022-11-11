package tn.supcom.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "addresses")
@AttributeOverride(name = "id", column = @Column(name = "address_id", columnDefinition = "BIGINT")) //columnDefinition optional
public class Address extends SimplePKEntity<Long>{
    private String addressLine1;
    private String addressLine2;
    private String district;
    private String postalCode;
    private String phone;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", columnDefinition = "SMALLINT UNSIGNED", referencedColumnName = "city_id", nullable = false)
    // name is the foreign key & referenceColumnName is the primary key
    private City city;

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Address address = (Address) o;
        return addressLine1.equals(address.addressLine1) && Objects.equals(addressLine2, address.addressLine2) && district.equals(address.district) && postalCode.equals(address.postalCode) && Objects.equals(phone, address.phone) && city.equals(address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), addressLine1, addressLine2, district, postalCode, phone, city);
    }

    @Override
    public String toString() {
        return "{" +
                "\"super\":" + super.toString() +
                ", \"addressLine1\":\"" + addressLine1 + '\"' +
                ", \"addressLine2\":\"" + addressLine2 + '\"' +
                ", \"district\":\"" + district + '\"' +
                ", \"postalCode\":\"" + postalCode + '\"' +
                ", \"phone\":\"" + phone + '\"' +
                ", \"city\":" + city +
                '}';
    }
}
