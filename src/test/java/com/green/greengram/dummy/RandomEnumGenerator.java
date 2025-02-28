package com.green.greengram.dummy;

public class RandomEnumGenerator<T extends Enum<T>> {
    private final T[] values;

    public RandomEnumGenerator(Class<T> e) {
        this.values = e.getEnumConstants();
    }

    public T getRandomEnum() {
        return values[(int)(Math.random() * values.length)];
    }
}
