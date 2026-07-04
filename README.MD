🎯 Когда что выбирать
Выбирайте Strategy + Type Map, если:
✅ Нужна поддержка наследников типов
✅ Нужны условные правила (предикаты)
✅ Нужны приоритеты / fallback
✅ Критична производительность вызова
✅ Мапперов много, и они динамически регистрируются
Выбирайте THC Блоха, если:
✅ Максимальная типобезопасность важнее всего
✅ Работаете только с точными типами (без наследования)
✅ Нужна академическая чистота кода
✅ Мапперов немного и они статичны
* содержит методы registry и map
* *Ваш applyMapper — это стандартный паттерн capture helper, он безопасен при условии, что registry заполняется только через типобезопасный register. Именно так устроены многие библиотеки (Jackson, Gson, Spring ConversionService)

## Дополнительные требования
* Любопытна работа типами с дженериками 
* Контейнерными типами
* С key-value типами

Для решения этих проблем возможно понадобится подход **Комбинация подходов с деградацией**:
```java
class HybridResolver {
    private TransformRegistry registry;  // 1. Явная регистрация
    private ConventionResolver convention;  // 2. Конвенции
    private AnnotationResolver annotations;  // 3. Аннотации
    private Transform defaultTransform;  // 4. Fallback
    
    public <S, T> Transform<S, T> resolve(Class<S> source, Class<T> target) {
        // 1. Проверяем явную регистрацию (самый быстрый путь)
        Transform t = registry.get(source, target);
        if (t != null) return t;
        
        // 2. Проверяем аннотации
        t = annotations.resolve(source, target);
        if (t != null) return t;
        
        // 3. Пытаемся конвенцию
        t = convention.resolve(source, target);
        if (t != null) return t;
        
        // 4. Fallback (рефлексия, копирование полей по имени)
        return defaultTransform;
    }
}
```
Также нужно разобрать тему гетерогенного контейнера с **TypeReference для сложных (дженерик) типов**
Если вам нужно возвращать List<UserEntity> или Map<String, OrderEntity>, обычный Class<T> не подойдет. На помощь приходит паттерн Super Type Token (используется в Gson, Jackson).

````java
// Абстрактный класс, который "запоминает" полный тип, включая дженерики
public abstract class TypeReference<T> {
    private final Type type;

    protected TypeReference() {
        // Магия рефлексии: получаем тип из сигнатуры анонимного подкласса
        this.type = ((ParameterizedType) getClass().getGenericSuperclass())
                     .getActualTypeArguments()[0];
    }

    public Type getType() { return type; }
}
// НЕ РАБОЧИЙ КОД!
public class MapperRegistry {
    private final Map<Class<?>, Mapper<?, ?>> registry = new HashMap<>();

    public <I, O> void register(Class<I> inputType, Mapper<I, O> mapper) {
        registry.put(inputType, mapper);
    }

    // Метод принимает TypeReference вместо Class
    public <T> T map(Object input, TypeReference<T> outputTypeRef) {
        Mapper<?, ?> rawMapper = registry.get(input.getClass());
        if (rawMapper == null) throw new IllegalArgumentException("Нет правила");

        return applyMapper(rawMapper, input, outputTypeRef);
    }

    private <I, O, T> T applyMapper(Mapper<I, O> mapper, Object input, TypeReference<T> outputTypeRef) {
        @SuppressWarnings("unchecked")
        I typedInput = (I) input;
        O result = mapper.map(typedInput);

        // Проверяем, что результат соответствует ожидаемому типу
        if (!matchesType(result, outputTypeRef.getType())) {
            throw new ClassCastException("Результат не соответствует ожидаемому типу");
        }

        @SuppressWarnings("unchecked")
        T typedResult = (T) result;
        return typedResult;
    }

    // Упрощенная проверка типа (в реальности нужна более сложная логика)
    private boolean matchesType(Object obj, Type expectedType) {
        if (expectedType instanceof Class) {
            return ((Class<?>) expectedType).isInstance(obj);
        }
        // Для ParameterizedType (List<String> и т.д.) нужна более сложная проверка
        return true;
    }
}
````

## Варианты реализаций
* простой, Джошу Блоху
* * который делает каст к целевому типу
* * не делает каст к целевому типу
* Для поддержки дженериков использовать ReferenceType - наследника для извлечения дженерик типа
* Использовать пару входного и ожидаемого выходного типа. Это позволит один тип мапить на разные выходные типы
* С контекстом Добавить как параметр метода маппинга сам экземпляр маппера, для трансформации к содержащимся внутри входного объекта вложенных объектов и управления обходом в том числе на основе хранищихся в самом мапере данных.
* С параметром состояния или родителя, для отслеживания, установки текущего положения или родителя. По аналогии с рефакторингом Джошу Кириевски но только где гомогенный TagHolder используется.
* IdentityMap для хранения ранее обнаруженных идентичных объектов для передачи их вновь созданным. Например когда несколько заказов ссылаются на одного пользователя.
* * Кстати может быть гетерогенным контейнером Блоха для хранения различных значений. Но вопрос с хранением наследников может быть.
* Стэк для хранения уже пройденных объектов для детектирования циклов