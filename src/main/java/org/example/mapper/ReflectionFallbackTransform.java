package org.example.mapper;

import org.example.Mapper;
import org.example.MapperContext;

import java.lang.reflect.Field;

public class ReflectionFallbackTransform implements Mapper<Object,Object> {
    @Override
    public Object map(Object src) {
        return null;
    }
    public Object transform(Object source, Class<?> targetType, MapperContext ctx) {
        try {
            // 1. Создаём целевой объект (конструктор по умолчанию)
            Object target = targetType.getDeclaredConstructor().newInstance();

            // 2. Копируем поля по именам
            for (Field srcField : source.getClass().getDeclaredFields()) {
                try {
                    Field tgtField = targetType.getDeclaredField(srcField.getName());

                    // Проверяем совместимость типов
                    if (isAssignable(srcField.getType(), tgtField.getType())) {
                        srcField.setAccessible(true);
                        tgtField.setAccessible(true);

                        Object srcValue = srcField.get(source);
                        Object tgtValue = srcValue;

                        // Если тип сложный — рекурсивный маппинг
                        if (needsMapping(srcField.getType(), tgtField.getType())) {
                            tgtValue = ctx.map(srcValue, tgtField.getType());
                        }

                        tgtField.set(target, tgtValue);
                    }
                } catch (NoSuchFieldException e) {
                    // Поле с таким именем не найдено в целевом типе — пропускаем
                }
            }

            return target;
        } catch (Exception e) {
            throw new RuntimeException("Fallback failed", e);
        }
    }

    private boolean needsMapping(Class<?> type, Class<?> type1) {
        return false;
    }

    private boolean isAssignable(Class<?> src, Class<?> tgt) {
        return tgt.isAssignableFrom(src) ||
                canConvert(src, tgt);  // например, String ↔ Integer
    }

    private boolean canConvert(Class<?> src, Class<?> tgt) {
        return false;
    }
}
