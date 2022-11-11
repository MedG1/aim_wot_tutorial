package tn.supcom.entities;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Character> {
    @Override
    public Character convertToDatabaseColumn(Gender gender) {
        if(gender == null) return null;
        return gender.getValue();
    }

    @Override
    public Gender convertToEntityAttribute(Character character) {
        return character.equals('M') ? Gender.MALE : Gender.FEMALE;
    }
}
