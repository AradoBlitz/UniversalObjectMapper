package org.example.service;

import org.example.ContextBasedMapper;
import org.example.Mapper;
import org.example.MapperContext;

import java.util.HashMap;
import java.util.Map;

public class ContextBasedMapperRegistry implements MapperContext {

    private record Pair(Class<?> input, Class<?> output) {};

    private Map<Pair, ContextBasedMapper<?,?>> registry = new HashMap<>();

    @Override
    public <O> O map(Object src, Class<O> outputType) {

        Pair key = new Pair(src.getClass(), outputType);

        if(!registry.containsKey(key)){
            throw new RuntimeException("No mapper for source");
        }

        ContextBasedMapper<?,?> mapper = registry.get(key);
        ContextBasedMapper<Object,Object> dangerousMapper = (ContextBasedMapper<Object, Object>) mapper;

        O result = outputType.cast(dangerousMapper.map(src,this));

        return result;
    }

    public <I,O> void register(Class<I> inputType, Class<?> outputType, ContextBasedMapper<I,O> mapper) {
        registry.put(new Pair(inputType,outputType),mapper);
    }
}
