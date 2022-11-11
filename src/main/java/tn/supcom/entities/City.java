package tn.supcom.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cities")
@AttributeOverride(name = "id", column = @Column(name = "city_id", columnDefinition = "SMALLINT UNSIGNED")) //columnDefinition optional
public class City extends SimplePKEntity<Integer>{
    private String city;
    @ManyToOne(fetch = FetchType.EAGER) //many cities to one country
    @JoinColumn(name = "country_id", columnDefinition = "TINYINT UNSIGNED", referencedColumnName = "country_id", nullable = false)
    private Country country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        City city1 = (City) o;
        return Objects.equals(city, city1.city) && Objects.equals(country, city1.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), city, country);
    }

    @Override
    public String toString() {
        return "{" +
                "\"super\":" + super.toString() +
                ", \"city\":\"" + city + '\"' +
                ", \"country\":\"" + country + '\"' +
                '}';
    }
}
