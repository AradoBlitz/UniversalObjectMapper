package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.model.UserDto;
import org.example.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

class SimpleMapperRegistryTest {

    private SimpleMapperRegistry simpleMapperRegistry = new SimpleMapperRegistry();

    @Test
    void mapUserEntity() {
        Object unknownObjectFromNetwork = new UserDto("Alice", 25);
        simpleMapperRegistry.register(UserDto.class, dto -> {
            UserEntity entity = new UserEntity("User: " + dto.getName(), dto.getAge()>18);
            return entity;
        });
        Assertions.assertThat(new UserEntity("User: Alice", Boolean.TRUE))
                .isEqualTo(simpleMapperRegistry.map(unknownObjectFromNetwork));
    }

    @Test
    void mapWithCastUserEntity() {
        Object unknownObjectFromNetwork = new UserDto("Alice", 25);
        simpleMapperRegistry.register(UserDto.class, dto -> {
            UserEntity entity = new UserEntity("User: " + dto.getName(), dto.getAge()>18);
            return entity;
        });
        UserEntity map = simpleMapperRegistry.map(unknownObjectFromNetwork,UserEntity.class);
        Assertions.assertThat(new UserEntity("User: Alice", Boolean.TRUE))
                .isEqualTo(map);
    }
}