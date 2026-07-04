package org.example;

public interface MapperContext {

    <O> O map(Object src, Class<O> outputType);
}
