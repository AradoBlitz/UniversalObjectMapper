package org.example.mapper;

import org.example.MapperContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapFallbackTransform { //implements Transform<Map<?, ?>, Map<?, ?>>

    public Map<?, ?> transform(Map<?, ?> source, Class<?> targetType, MapperContext ctx) {
        Map<Object, Object> result = new HashMap<>();

        for (Map.Entry<?, ?> entry : source.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            // Трансформируем ключ и значение
            Object newKey = transformValue(key, ctx);
            Object newValue = transformValue(value, ctx);

            result.put(newKey, newValue);
        }

        return result;
    }

    private Object transformValue(Object value, MapperContext ctx) {
        if (value instanceof Map) {
            return ctx.map((Map<?, ?>) value, Map.class);
        } else if (value instanceof List) {
            return ((List<?>) value).stream()
                    .map(v -> transformValue(v, ctx))
                    .collect(Collectors.toList());
        } /*else if (value instanceof ASN1Type) {
            return ctx.map((ASN1Type) value, Object.class);
        }*/
        return value;
    }
}
