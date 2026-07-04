package org.example;

public interface ContextBasedMapper<I,O> {

    O map(I input, MapperContext mapper);
}
