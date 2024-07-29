package com.buybazaar.userservice.service;

import com.buybazaar.userservice.dto.RoleDTO;
import com.buybazaar.userservice.dto.UserDTO;
import com.buybazaar.userservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    UserDTO createUser(UserDTO userDTO);
    Optional<User> getUserById(Long id);
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    void assignRoleToUser(Long userId, RoleDTO roleDTO);

}
