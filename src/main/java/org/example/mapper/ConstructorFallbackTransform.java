package org.example.mapper;

import org.example.MapperContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ConstructorFallbackTransform {
    public Object transform(Object source, Class<?> targetType, MapperContext ctx) {
        // Ищем конструктор с наибольшим совпадением параметров
        Constructor<?> bestConstructor = findBestConstructor(source.getClass(), targetType);

        if (bestConstructor == null) {
            throw new RuntimeException("No matching constructor for " + targetType);
        }

        try {
            Object[] args = new Object[bestConstructor.getParameterCount()];
            Class<?>[] paramTypes = bestConstructor.getParameterTypes();

            for (int i = 0; i < paramTypes.length; i++) {
                Field srcField = findFieldByName(source.getClass(), getParamName(bestConstructor, i));
                srcField.setAccessible(true);
                Object srcValue = srcField.get(source);

                if (needsMapping(srcField.getType(), paramTypes[i])) {
                    args[i] = ctx.map(srcValue, paramTypes[i]);
                } else {
                    args[i] = srcValue;
                }
            }

            return bestConstructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Constructor mapping failed", e);
        }
    }

    private Field findFieldByName(Class<?> aClass, Object paramName) {
        return null;
    }

    private boolean needsMapping(Class<?> type, Class<?> paramType) {
        return false;
    }

    private Object getParamName(Constructor<?> bestConstructor, int i) {
        return null;
    }

    private Constructor<?> findBestConstructor(Class<?> aClass, Class<?> targetType) {
        return null;
    }
}
