package com.example.cinema.core;

public enum SeatType {
    BLIND(0.5d),
    LUXURY(1.2d),
    KISSES(1.5d);


    private final Double coefficient;


    SeatType(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Double getCoefficient() {
        return coefficient;
    }
}
