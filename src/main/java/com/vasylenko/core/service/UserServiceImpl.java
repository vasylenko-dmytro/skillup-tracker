package com.vasylenko.core.service;

import com.vasylenko.core.dto.UserDto;
import com.vasylenko.core.dto.UserMapper;
import com.vasylenko.core.entity.Role;
import com.vasylenko.core.entity.User;
import com.vasylenko.core.enums.Roles;
import com.vasylenko.core.exception.RecordNotFoundException;
import com.vasylenko.core.repository.RoleRepository;
import com.vasylenko.core.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @Override
    public void saveUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        Role role = roleRepository.findByName(String.valueOf(Roles.ROLE_USER));
        if (role == null) {
            role = setDefaultRole();
        }
        user.setRoles(List.of(role));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(userDto.getFirstName()
                    .concat(" ")
                    .concat(userDto.getLastName()));
            existingUser.setEmail(userDto.getEmail());
            return userMapper.toDto(userRepository.save(existingUser));
            })
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            User user = userById.get();
            user.setRoles(null);
            userRepository.delete(user);
        } else {
            throw new IllegalArgumentException("User not found!");
        }
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email));
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    private Role setDefaultRole() {
        Role role = new Role();
        role.setName(String.valueOf(Roles.ROLE_USER));
        return roleRepository.save(role);
    }

    public UserDto create(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail());
        User user = userMapper.toEntity(userDto);
        Role role = roleRepository.findByName(String.valueOf(Roles.ROLE_USER));
        if (role == null) {
            role = setDefaultRole();
        }
        user.setRoles(List.of(role));
        return userMapper.toDto(userRepository.save(user));
    }

}
