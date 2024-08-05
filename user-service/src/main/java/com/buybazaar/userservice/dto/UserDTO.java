package com.buybazaar.userservice.dto;

import lombok.Data;

import java.util.Optional;
import java.util.Set;

@Data
public class UserDTO {

    private String username;
    private String password;
    private String email;
    private String fullname;
    private Set<String> roles;
}
