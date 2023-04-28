package com.vasylenko.core.service;

import com.vasylenko.core.dto.UserDto;
import com.vasylenko.core.entity.Role;
import com.vasylenko.core.entity.User;
import com.vasylenko.core.enums.Roles;
import com.vasylenko.core.repository.RoleRepository;
import com.vasylenko.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName(String.valueOf(Roles.ROLE_USER));
        if (role == null) {
            role = setDefaultRole();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private Role setDefaultRole() {
        Role role = new Role();
        role.setName(String.valueOf(Roles.ROLE_USER));
        return roleRepository.save(role);
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
    String[] str = user.getName().split("\\s");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
