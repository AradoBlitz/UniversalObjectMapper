package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.model.OrderDto;
import org.example.model.OrderEntity;
import org.example.model.UserDto;
import org.example.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ContextBasedMapperTest {

    private ContextBasedMapperRegistry mapper = new ContextBasedMapperRegistry();

    @Test
    void mapWithCastUserEntity() {

        UserDto userDto = new UserDto("Alice", 25);
        Object unknownObjectFromNetwork = new OrderDto("777", 3, userDto);

        OrderEntity map = mapper.map(unknownObjectFromNetwork, OrderEntity.class);
        assertThat(map)
                .isEqualTo(new OrderEntity(777L,
                        300,
                        new UserEntity("User: Alice",
                                Boolean.TRUE)));
    }

    @Test
    void classCastException() {

        UserDto userDto = new UserDto("Alice", 25);
        Object unknownObjectFromNetwork = new OrderDto("777", 3, userDto);

        assertThatThrownBy(() -> mapper.map(unknownObjectFromNetwork, UserEntity.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No mapper for source");

    }

    @BeforeEach
    public void setupMappers() {
        mapper.register(UserDto.class, UserEntity.class, (dto, context) -> {
            UserEntity entity = new UserEntity("User: " + dto.getName(), dto.getAge() > 18);
            return entity;
        });
        mapper.register(OrderDto.class, OrderEntity.class, (dto, context) -> {
            OrderEntity order = new OrderEntity(Long.parseLong(dto.getId()),
                    (int) (dto.getSum() * 100),
                    context.map(dto.getUserDto(), UserEntity.class));
            return order;
        });
    }
}
