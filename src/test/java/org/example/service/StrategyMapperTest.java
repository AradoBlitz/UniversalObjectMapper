package org.example.service;

import org.assertj.core.api.Assertions;
import org.example.Mapper;
import org.example.model.UserDto;
import org.example.model.UserEntity;
import org.junit.jupiter.api.Test;

public class StrategyMapperTest {

    private StrategyMapper mapper = new StrategyMapper();

    @Test
    void mapUserEntity() {
        Object unknownObjectFromNetwork = new UserDto("Alice", 25);
        mapper.register(UserDto.class, dto -> {
            UserEntity entity = new UserEntity("User: " + dto.getName(), dto.getAge()>18);
            return entity;
        });
        UserEntity map = mapper.map(unknownObjectFromNetwork,UserEntity.class);
        Assertions.assertThat(new UserEntity("User: Alice", Boolean.TRUE))
                .isEqualTo(map);
    }
}
