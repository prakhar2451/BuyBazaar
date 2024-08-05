package com.buybazaar.userservice.service.serviceimpl;

import com.buybazaar.userservice.dto.RoleDTO;
import com.buybazaar.userservice.dto.UserDTO;
import com.buybazaar.userservice.entity.Role;
import com.buybazaar.userservice.entity.User;
import com.buybazaar.userservice.exception.ResourceNotFoundException;
import com.buybazaar.userservice.exception.UserAlreadyExistsException;
import com.buybazaar.userservice.repository.RoleRepository;
import com.buybazaar.userservice.repository.UserRepository;
import com.buybazaar.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        try {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                logger.warn("User with email {} already exists", userDTO.getEmail());
                throw new UserAlreadyExistsException("User with this email already exists.");
            }

            User user = modelMapper.map(userDTO, User.class);

            if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
                Set<Role> roles = userDTO.getRoles().stream()
                        .map(roleName -> {
                            Role role = roleRepository.findByName(roleName);
                            if (role == null) {
                                throw new IllegalArgumentException("Role not found: " + roleName);
                            }
                            return role;
                        })
                        .collect(Collectors.toSet());
                user.setRoles(roles);
                logger.info("Roles assigned: " + roles);
            }
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            User savedUser = userRepository.save(user);

            UserDTO createdUserDTO = modelMapper.map(savedUser, UserDTO.class);
            logger.info("User created successfully with email {}", createdUserDTO.getEmail());
            return createdUserDTO;

        } catch (Exception e) {

            logger.error("Error occurred while creating user: {}",e.getMessage(),e);
            throw new RuntimeException("Error occurred while creating user.");
        }
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "ID", id));

            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            logger.info("Fetched user with ID: {}", id);
            return Optional.of(userDTO);

        } catch (ResourceNotFoundException e) {
            logger.error("User not found with ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while fetching user with ID: {}", id, e);
            throw new RuntimeException("Error occurred while fetching user.");
        }
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "ID", id));

            existingUser.setUsername(userDTO.getUsername());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setFullName(userDTO.getFullname());
            existingUser.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(existingUser);
            UserDTO updatedUserDTO = modelMapper.map(updatedUser, UserDTO.class);
            logger.info("Updated user with ID: {}", id);
            return updatedUserDTO;

        } catch (ResourceNotFoundException e) {
            logger.error("User not found with ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while updating user with ID: {}", id, e);
            throw new RuntimeException("Error occurred while updating user.");
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "ID", id));

            userRepository.delete(user);
            logger.info("Deleted user with ID: {}", id);

        } catch (ResourceNotFoundException e) {
            logger.error("User not found with ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deleting user with ID: {}", id, e);
            throw new RuntimeException("Error occurred while deleting user.");
        }
    }

    @Override
    public UserDTO findByUsername(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));

            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            logger.info("Fetched user with username: {}", username);
            return userDTO;

        } catch (ResourceNotFoundException e) {
            logger.error("User not found with username: {}", username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while fetching user with username: {}", username, e);
            throw new RuntimeException("Error occurred while fetching user.");
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));

            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            logger.info("Fetched user with email: {}", email);
            return userDTO;

        } catch (ResourceNotFoundException e) {
            logger.error("User not found with email: {}", email, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while fetching user with email: {}", email, e);
            throw new RuntimeException("Error occurred while fetching user.");
        }
    }

    @Override
    public void assignRoleToUser(Long userId, RoleDTO roleDTO) {
        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

            Role role = roleRepository.findById(roleDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "ID", roleDTO.getId()));

            user.getRoles().add(role);

            userRepository.save(user);

            logger.info("Role {} assigned to user with ID: {}", role.getName(), userId);

        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found while assigning role to user with ID: {}", userId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while assigning role to user with ID: {}", userId, e);
            throw new RuntimeException("Error occurred while assigning role to user.");
        }
    }

    @Override
    public UserDTO findById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "ID", id));

            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            logger.info("Fetched user with id: {}", id);
            return userDTO;

        } catch (ResourceNotFoundException e) {
            logger.error("User not found with id: {}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while fetching user with email: {}", id, e);
            throw new RuntimeException("Error occurred while fetching user.");
        }
    }

}
