package org.example.mapper;

/*
* Зачем нужен typeStack?
Причина	Пример
Защита от циклов	User → Address → User (бесконечная рекурсия)
Отладка	При ошибке показать полный путь: MailCategoryASN1 → MailCategory → Code → CodeType
Контекстная логика	Разное поведение на разной глубине (например, ленивая загрузка)
Условная трансформация	if (ctx.getDepth() > 3) useLazyLoading()
* */
public class CycleTransform {
    /*
    *  public UserDto transform(UserEntity src, MappingContext ctx) {
        UserDto dto = new UserDto();
        dto.setName(src.getName());

        // Проверка: не маппим ли мы уже этот объект?
        if (ctx.willCauseCycle(src.getAddress(), AddressDto.class)) {
            dto.setAddress(null);  // или ленивая ссылка
        } else {
            dto.setAddress(ctx.map(src.getAddress(), AddressDto.class));
        }

        return dto;
    }
    * */

    /**
     * Получить текущий путь маппинга (для отладки/ошибок)
     */
   /* public String getMappingPath() {
        return typeStack.stream()
                .map(p -> p.source.getSimpleName() + "→" + p.target.getSimpleName())
                .collect(Collectors.joining(" → "));
    }*/

    /**
     * Получить глубину вложенности
     */
   /* public int getDepth() {
        return typeStack.size();
    }*/
}
