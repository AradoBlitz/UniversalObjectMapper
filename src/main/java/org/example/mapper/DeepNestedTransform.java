package org.example.mapper;

/*
* Компонент	Назначение
Fallback	Когда нет явного маппинга (reflection, bean utils, constructor)
typeStack	Защита от циклов, отладка, контекстная логика
instanceCache	Обработка циклических ссылок (один объект → один результат)
TransformRegistry	Централизованное управление стратегиями
* */
public class DeepNestedTransform {
/*
    public ComplexDto transform(ComplexASN1 src, MappingContext ctx) {
        ComplexDto dto = new ComplexDto();

        // Разная логика на разной глубине
        if (ctx.getDepth() > 5) {
            // Глубокая вложенность — используем ленивую загрузку
            dto.setLazyLoad(true);
        }

        // Логирование пути при ошибке
        try {
            dto.setNested(ctx.map(src.getNested(), NestedDto.class));
        } catch (Exception e) {
            throw new MappingException(
                    "Failed at path: " + ctx.getMappingPath(),
                    e
            );
        }

        return dto;
    }*/

    /**
     * Получение трансформера с кэшированием
     */
   /* private Transform<?, ?> getTransform(TypePair pair) {
        // Кэш ускоряет повторные вызовы
        return cache.computeIfAbsent(pair, p -> registry.resolve(p));
        //                              ↑
        //                         здесь вызывается registry.resolve(p)
    }*/

    // Утилиты для отладки
   /* public int getDepth() {
        return typeStack.size();
    }

    public String getMappingPath() {
        return typeStack.stream()
                .map(p -> p.source.getSimpleName() + "→" + p.target.getSimpleName())
                .collect(Collectors.joining(" → "));
    }

    public boolean willCauseCycle(Object source, Class<?> targetType) {
        return typeStack.contains(new TypePair(source.getClass(), targetType));
    }*/
}
