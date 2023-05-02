package com.vasylenko.core.dto;

import com.vasylenko.core.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDto userDto) {

        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getFirstName()
                .concat(" ")
                .concat(userDto.getLastName()));
        user.setEmail(userDto.getEmail());

        return user;
    }

    public UserDto toDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        String[] fullName = user.getName().split(" ");
        userDto.setFirstName(fullName[0]);
        userDto.setLastName(fullName[1]);
        userDto.setEmail(user.getEmail());

        return userDto;
    }

}
