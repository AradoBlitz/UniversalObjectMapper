package org.example.mapper;

import org.example.MapperContext;

import java.lang.reflect.Method;
import java.util.Map;

public class BeanUtilsFallbackTransform {

    public Object transform(Object source, Class<?> targetType, MapperContext ctx) {
        Object target = instantiate(targetType);

        // Собираем все сеттеры целевого класса
        Map<String, Method> setters = findSetters(targetType);

        // Проходим по геттерам исходного класса
        for (Method getter : source.getClass().getMethods()) {
            if (!isGetter(getter)) continue;

            String propertyName = extractPropertyName(getter.getName());  // "getName" → "name"
            Method setter = setters.get(propertyName);

            if (setter != null && isCompatible(getter.getReturnType(), setter.getParameterTypes()[0])) {
                try {
                    Object value = getter.invoke(source);

                    // Рекурсивный маппинг для сложных типов
                    if (needsMapping(getter.getReturnType(), setter.getParameterTypes()[0])) {
                        value = ctx.map(value, setter.getParameterTypes()[0]);
                    }

                    setter.invoke(target, value);
                } catch (Exception e) {
                    // Пропускаем проблемные поля
                }
            }
        }

        return target;
    }

    private Map<String, Method> findSetters(Class<?> targetType) {
        return null;
    }

    private Object instantiate(Class<?> targetType) {
        return null;
    }

    private boolean needsMapping(Class<?> returnType, Class<?> parameterType) {
        return false;
    }

    private boolean isCompatible(Class<?> returnType, Class<?> parameterType) {
        return false;
    }

    private boolean isGetter(Method m) {
        return m.getName().startsWith("get") &&
                m.getParameterCount() == 0 &&
                !void.class.equals(m.getReturnType());
    }

    private String extractPropertyName(String getterName) {
        String prop = getterName.substring(3);  // убираем "get"
        return Character.toLowerCase(prop.charAt(0)) + prop.substring(1);
    }
}
