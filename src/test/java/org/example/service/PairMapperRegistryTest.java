package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.model.UserDto;
import org.example.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

class PairMapperRegistryTest {

    private PairMapperRegistry simpleMapperRegistry = new PairMapperRegistry();

    @Test
    void map() {
        Object unknownObjectFromNetwork = new UserDto("Alice", 25);
        simpleMapperRegistry.register(UserDto.class, UserEntity.class ,dto -> {
            UserEntity entity = new UserEntity("User: " + dto.getName(), dto.getAge()>18);
            return entity;
        });
        UserEntity map = simpleMapperRegistry.map(unknownObjectFromNetwork,UserEntity.class);
        Assertions.assertThat(map)
                .isEqualTo(new UserEntity("User: Alice", Boolean.TRUE));
    }

}