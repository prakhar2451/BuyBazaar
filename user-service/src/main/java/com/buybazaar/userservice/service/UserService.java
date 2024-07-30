package com.buybazaar.userservice.service;

import com.buybazaar.userservice.dto.RoleDTO;
import com.buybazaar.userservice.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    UserDTO createUser(UserDTO userDTO);
    Optional<UserDTO> getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO findByUsername(String username);
    UserDTO findByEmail(String email);
    void assignRoleToUser(Long userId, RoleDTO roleDTO);
    UserDTO findById(Long id);
}
