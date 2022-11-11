package tn.supcom.entities;

public enum Gender {
    MALE('M'), FEMALE('F');
    private final Character value;
    Gender(Character value){
        this.value = value;
    }

    public Character getValue() {
        return value;
    }


}
