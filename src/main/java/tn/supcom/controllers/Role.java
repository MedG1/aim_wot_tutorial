package tn.supcom.controllers;

public enum Role {
    Surfer(1L),
    Client(1L<<1L),
    Accountant(1L<<2L),
    Commercial(1L<<3L),
    Administrator(1L<<4L);

    private final long value;
    Role(long value){
        this.value = value;
    }

    public long getValue() {
        return value;
    }

}
