package com.example.backend.service;

import com.example.backend.dto.request.UserRequest;
import com.example.backend.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse findUserById(Integer id);
    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(Integer id, UserRequest userRequest);
    void deleteUser(Integer id);
}
