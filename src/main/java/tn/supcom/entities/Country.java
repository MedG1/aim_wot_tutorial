package tn.supcom.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "countries")
@AttributeOverride(name = "id", column = @Column(name = "country_id", columnDefinition = "TINYINT UNSIGNED")) //columnDefinition optional
public class Country extends SimplePKEntity<Short>{
    @Column(nullable = false, unique = true)
    private String country;
    @XmlTransient
    @JsonbTransient
    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER) //country is the attribute of the City class
    private Set<City> cities = new HashSet<>();


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void addCity(City city) {
        city.setCountry(this);
        this.cities.add(city);
    }

    public void removeCity(City city) {
        city.setCountry(null);
        this.cities.remove(city);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Country country1 = (Country) o;
        return country.equals(country1.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), country);
    }

    @Override
    public String toString() {
        return "{" +
                "\"super\":" + super.toString() +
                ", \"country\":\"" + country + '\"' +
                '}';
    }
}
