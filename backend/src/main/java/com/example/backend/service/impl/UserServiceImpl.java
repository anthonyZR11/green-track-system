package com.example.backend.service.impl;

import com.example.backend.dto.request.UserRequest;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.entities.User;
import com.example.backend.enums.Role;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findByDeletedAtIsNull().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    public UserResponse findUserById(Integer id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return convertToResponse(user);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new DuplicateResourceException("El username ya existe");
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("El email ya existe");
        }
        User user = User.builder()
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.valueOf(userRequest.getRole()))
                .build();

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest userRequest) {

        User userData = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (!userData.getUsername().equals(userRequest.getUsername()) &&
                userRepository.existsByUsername(userRequest.getUsername())) {
            throw new DuplicateResourceException("El username ya existe");
        }

        if (!userData.getEmail().equals(userRequest.getEmail()) &&
                userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("El email ya existe");
        }

        User user = User.builder()
            .id(userData.getId())
            .name(userRequest.getName())
            .username(userRequest.getUsername())
            .email(userRequest.getEmail())
            .password(userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()
                ? passwordEncoder.encode(userRequest.getPassword())
                : userData.getPassword())
            .role(Role.valueOf(userRequest.getRole()))
            .build();

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        userRepository.delete(user);
    }

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
