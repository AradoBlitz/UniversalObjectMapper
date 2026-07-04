package org.example.service;

import org.example.Mapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/*
 *  «Типобезопасный гетерогенный контейнер» (Type-Safe Heterogeneous Container)
 * */
public class SimpleMapperRegistry {

    private Map<Class<?>, Mapper<?, ?>> registry = new HashMap<>();

    public <I, O> void register(Class<I> input, Mapper<I, O> mapper) {
        registry.put(input, mapper);
    }

    public <I, O> Mapper<I, O> getMapper(Class<I> inputType) {
        return (Mapper<I, O>) registry.get(inputType);
    }

    public Object map(Object src) {

        Class<?> inputType = src.getClass();
        Mapper<?, ?> rawMapper = registry.get(inputType);
        // Кастим сам мапер к "сырому" типу Object -> Object
        //Внутри applyMapper мы делаем (I) input. Компилятор ругается (Unchecked cast), поэтому мы вешаем @SuppressWarnings.
        @SuppressWarnings("unchecked") //Необязательно но на всякий случай.
        Mapper<Object, Object> dangerousMapper = (Mapper<Object, Object>) rawMapper;

        return dangerousMapper.map(src);
    }

    public <T> T map(Object src, Class<T> outputType) {
        return outputType.cast(map(src));
    }
}
