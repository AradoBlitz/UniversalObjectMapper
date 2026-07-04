package org.example;

public interface Mapper <I,O>{

    // Вспомогательный метод-помощник (Capture Helper)
    // Он нужен, чтобы "поймать" wildcard и дать компилятору поработать с типами
    // Компилятор создает для этого конкретного вызова "синтетические" типы I и O
    // Каст (I) input (то есть (UserDto) input) всегда пройдет успешно.
      static <I,O> O applyMapper(Mapper<?,?> rawMapper, I input) {
        Mapper<I,O> mapper = (Mapper<I,O>) rawMapper;
        return mapper.map(input);
    }

    O map(I src);
}
