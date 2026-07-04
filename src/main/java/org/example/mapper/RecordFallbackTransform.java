package org.example.mapper;

import org.example.MapperContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;

public class RecordFallbackTransform {
    public Object transform(Object source, Class<?> targetType, MapperContext ctx) {
        if (!targetType.isRecord()) {
            throw new RuntimeException("Target is not a record");
        }

        try {
            // Получаем компоненты рекорда
            RecordComponent[] components = targetType.getRecordComponents();
            Object[] args = new Object[components.length];

            for (int i = 0; i < components.length; i++) {
                RecordComponent component = components[i];
                String name = component.getName();
                Class<?> type = component.getType();

                // Ищем поле с таким же именем в источнике
                Field srcField = findFieldByName(source.getClass(), name);
                srcField.setAccessible(true);

                Object srcValue = srcField.get(source);

                if (needsMapping(srcField.getType(), type)) {
                    args[i] = ctx.map(srcValue, type);
                } else {
                    args[i] = srcValue;
                }
            }

            // Находим канонический конструктор рекорда
            Constructor<?> ctor = targetType.getDeclaredConstructor(
                    Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new)
            );

            return ctor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Record mapping failed", e);
        }
    }

    private boolean needsMapping(Class<?> type, Class<?> type1) {
        return false;
    }

    private Field findFieldByName(Class<?> aClass, String name) {
        return null;
    }
}
