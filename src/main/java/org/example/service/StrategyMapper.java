package org.example.service;

import org.example.Mapper;
import org.example.model.UserDto;
import org.example.model.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StrategyMapper {

    private final Map<Class<?>, Function<Object, Object>> strategies = new HashMap<>();

    public <I,O >void register(Class<I> srcType, Mapper<I,O> mapper) {
        strategies.put(srcType, input ->{
            // Кастим вход к нужному типу (безопасно, т.к. ключ = inputType)
            // @SuppressWarnings("unchecked")
            I typedInput = srcType.cast(input);

            // Применяем оригинальный типизированный маппер
            O result = mapper.map(typedInput);

            return result; // Возвращается как Object
        });
    }

    public <O> O map(Object input, Class<O> outputType) {

        Function<Object, Object> strategy = strategies.get(input.getClass());

        Object result = strategy.apply(input);

        return outputType.cast(result);
    }

    /**
     * Упрощенная версия — если мы доверяем выходному типу
     */
    public Object map(Object input) {
        if (input == null) return null;

        Function<Object, Object> strategy = strategies.get(input.getClass());
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Нет стратегии для типа: " + input.getClass().getName()
            );
        }

        return strategy.apply(input);
    }
}
