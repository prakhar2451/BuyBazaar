package com.buybazaar.userservice.service.serviceimpl;

import com.buybazaar.userservice.dto.RoleDTO;
import com.buybazaar.userservice.dto.UserDTO;
import com.buybazaar.userservice.entity.User;
import com.buybazaar.userservice.exception.UserAlreadyExistsException;
import com.buybazaar.userservice.repository.UserRepository;
import com.buybazaar.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

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
    public Optional<User> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public void assignRoleToUser(Long userId, RoleDTO roleDTO) {

    }
}
