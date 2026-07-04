package org.example.service;

import org.example.Mapper;
import org.example.model.UserDto;
import org.example.model.UserEntity;

import java.util.HashMap;
import java.util.Map;

public class PairMapperRegistry {

    private record MapperKey(Class<?> input, Class<?> output){}

    private final Map<MapperKey,Mapper<?,?>> registry = new HashMap<>();

    public <I,O> void register(Class<I> input, Class<O> output, Mapper<I,O> mapper) {
        registry.put(new MapperKey(input, output), mapper);
    }

    public <I,O> O map(I src, Class<O> outputType) {
        Class<I> inputType = (Class<I>) src.getClass();

        Mapper<?,?> rawMapper = registry.get(new MapperKey(inputType, outputType));
        return Mapper.applyMapper(rawMapper,src);
    }

    class MapperRegistry {

        // ВОТ ОНО! Хранилище правил.
        // Мы не знаем типов на этапе компиляции, поэтому используем <?>
        private final Map<Class<?>, Mapper<?, ?>> registry = new HashMap<>();

        // Метод для регистрации правила
        public <I, O> void register(Class<I> inputType, Mapper<I, O> mapper) {
            registry.put(inputType, mapper);
        }

        // Метод для вызова (самое интересное!)
        public Object map(Object input) {
            if (input == null) return null;

            Class<?> inputType = input.getClass();

            // Достаем мапер. Компилятор видит его как Mapper<?, ?>
            Mapper<?, ?> rawMapper = registry.get(inputType);

            if (rawMapper == null) {
                throw new IllegalArgumentException("Нет правила для типа: " + inputType);
            }

            // Магия вызова: мы должны "кастнуть" wildcard к конкретным типам.
            // Мы знаем, что это безопасно, потому что ключ (inputType) гарантирует соответствие.
            return Mapper.applyMapper(rawMapper, input);
        }
    }
}
